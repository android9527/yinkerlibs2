package com.jlc.buried.manager;

import android.content.Context;
import android.text.TextUtils;

import com.jlc.buried.config.BaseUrl;
import com.jlc.buried.config.Config;
import com.jlc.buried.config.EnumType;
import com.jlc.buried.database.ModuleDb;
import com.jlc.buried.model.Access;
import com.jlc.buried.network.AsyncHttpRequest;
import com.jlc.buried.network.NetWorkCallBack;
import com.jlc.buried.network.config.ConnectionType;
import com.jlc.buried.network.config.NetCode;
import com.jlc.buried.network.model.ResponseData;
import com.jlc.buried.utils.Base64Util;
import com.jlc.buried.utils.GZipUtils;
import com.jlc.buried.utils.JsonUtil;
import com.jlc.buried.utils.LogUtil;
import com.jlc.buried.utils.ThreadPoolUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author deliang.xie
 * @data 2016/5/26 0026
 * @time 下午 8:14
 */
public class SDKMgrPrivate implements NetWorkCallBack, TaskCallBack {

    private static final String TAG = SDKMgrPrivate.class.getSimpleName();
    private ModuleDb db;
    private String appKey;
    private Map<String, Object> keyMap;


    public SDKMgrPrivate(Context context) {
        this.db = new ModuleDb(context);
        this.keyMap = new HashMap<>();
        this.appKey = "";
    }

    /**
     * 获取埋点数据
     *
     * @return
     */
    private String getModuleDataList() {
        String result = "";
        try {
            List<String> dataList = db.getModuleDataList();
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for (String json : dataList) {
                sb.append(json);
                sb.append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
            LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--获取采集数据,压缩前data= " + sb.toString());
            byte[] bytes = GZipUtils.gzipCompress(sb.toString().getBytes());
            result = URLEncoder.encode(new String(Base64Util.encBase64(bytes)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--获取采集数据,压缩后data= " + result);
        return result;
    }

    /**
     * 保存AppKey
     *
     * @param appKey
     */
    public void init(String appKey) {
        // 保存到本地
        this.appKey = appKey;
        LogUtil.e("SDK_MODULE", "初始化appKey---" + appKey);
        ThreadPoolUtils.execute(new TaskItem(TaskType.INIT, this));
    }

    /**
     * 获取AppKey
     *
     * @return
     */
    private String getAppKey() {
        LogUtil.e("SDK_MODULE", "SDKPrivate--getAppKey()--获取本地appKey");
        if (TextUtils.isEmpty(this.appKey)) {
            this.appKey = db.getAppKey();
        }
        return this.appKey;
    }

    /**
     * 获取Rid
     *
     * @return
     */
    private int getRid() {
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    /**
     * 保存埋点数据
     */
    public void saveModule(String key, String moduleStr) {
        LogUtil.e("SDK_MODULE", "DB--saveModule()--保存采集数据");
        ThreadPoolUtils.execute(new TaskItem(TaskType.SAVE_MODULE, this, key, moduleStr));
    }

    /**
     * 获取keyMap
     *
     * @return
     */
    private Map getKeyMap() {
        if (keyMap == null || keyMap.isEmpty()) {
            keyMap = JsonUtil.json2Map(db.getModuleStatus());
        }
        return keyMap;
    }

    /**
     * int type,
     * Map<String, Object> params,
     * NetWorkCallBack callBack,
     * String url
     */
    public void uploadData() {
        LogUtil.e("SDK_MODULE", "DB--uploadData()--开始上传数据");
        ThreadPoolUtils.execute(new TaskItem(TaskType.UPDATE_ACCESS, this));
    }


    /**
     * 鉴权后上报数据
     */
    private void requestUpdateModel(int rid, String ticket) {//上传
        LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--有上传数据--开始拼接上传接口参数");
        ThreadPoolUtils.execute(new TaskItem(TaskType.UPDATE_UPDATE, this, rid, ticket));
    }

    @Override
    public void onResponse(ResponseData data) {
        if (data == null) {
            return;
        }
        int type = data.getConnectionType();
        if (type == ConnectionType.TYPE_ACCESS) {
            LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--鉴权成功");
            handleNetAccess(data);
        } else if (type == ConnectionType.TYPE_UPLOAD) {
            LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--上传成功");
            handleNetUpdateModel(data);
        }
    }

    /**
     * 处理鉴权网络回掉
     *
     * @param data
     */
    private void handleNetAccess(ResponseData data) {
        if (data.status == NetCode.SUCCESS) {
            String jsonStr = data.getResponseData();
            Access parse = new Access();
            parse.parseData(jsonStr);
            keyMap = JsonUtil.json2Map(parse.getModuleStatus());
            // 鉴权成功后上传数据
            if (hasModuleData()) {// 采集数据不为空
                requestUpdateModel(data.rid, parse.getTicket());
            }
            // 保存Access数据
            ThreadPoolUtils.execute(new TaskItem(TaskType.SAVE_ACCESS, this, parse));
        } else {
            // 获取本地Tiecke 和过期时间进行上传
            ThreadPoolUtils.execute(new TaskItem(TaskType.GET_ACCESS, this));
            LogUtil.e(TAG, data.desc);
        }
    }

    private boolean hasModuleData() {
        String b = db.hasModuleData() == true ? "true" : "false";
        LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--判断是否有需要上传的数据" + b);
        return db.hasModuleData();
    }

    /**
     * 处理上报网络回掉
     *
     * @param data
     */
    private void handleNetUpdateModel(ResponseData data) {
        if (data.status == NetCode.SUCCESS) {
            // 清库
            LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--清除本地采集数据");
            ThreadPoolUtils.execute(new TaskItem(TaskType.CLEAR_DATA, this));
        } else {
            LogUtil.e(TAG, data.desc);
        }
    }

    @Override
    public void onNetWorkError(String str) {
        LogUtil.e(TAG, str);
    }

    @Override
    public Object onTaskExecute(int type, Object... objects) {
        if (type == TaskType.INIT) {//初始化
            db.saveAppKey(appKey);
        } else if (type == TaskType.UPDATE_ACCESS) {
            LogUtil.e("SDK_MODULE", "DB--uploadData()--拼接鉴权接口参数");
            Map<String, Object> params = new HashMap<>();
            params.put("appKey", getAppKey());
            params.put("rid", getRid());
            params.put("version", Config.API_VERSION);
            return params;
        } else if (type == TaskType.UPDATE_UPDATE) {
            if (objects != null && objects.length >= 2) {
                Map<String, Object> params = new HashMap<>();
                params.put("rid", objects[0]);
                params.put("version", Config.API_VERSION);
                params.put("ticket", objects[1]);
                params.put("data", getModuleDataList());
                return params;
            }
            return null;
        } else if (type == TaskType.SAVE_MODULE) {
            if (objects != null && objects.length >= 2) {
                String key = (String) objects[0];
                if (getKeyMap().containsKey(key)) {
                    double d = (double) keyMap.get(key);
                    if ((int) d == EnumType.ModuleStatus.OPEN.value) {// 开启状态
                        LogUtil.e("SDK_MODULE", "保存数据" + key + "开关开启");
                        db.saveModuleData((String) objects[1]);
                    }
                }
            }
        } else if (type == TaskType.SAVE_ACCESS) {
            if (objects != null && objects.length >= 1) {
                Access parse = (Access) objects[0];
                db.saveAccess(parse);
            }
        } else if (type == TaskType.CLEAR_DATA) {
            db.clearModuleData();
        } else if (type == TaskType.GET_ACCESS) {
            return db.getAccess();
        }
        return null;
    }

    @Override
    public void onTaskComplete(int type, Object obj) {
        if (type == TaskType.UPDATE_ACCESS) {
            if (obj == null) {
                return;
            }
            Map<String, Object> params = (Map<String, Object>) obj;
            LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--参数拼接完成，开始鉴权");
            AsyncHttpRequest.requestByPost(ConnectionType.TYPE_ACCESS, params, this, BaseUrl.ACCESS);
        } else if (type == TaskType.UPDATE_UPDATE) {
            if (obj == null) {
                return;
            }
            Map<String, Object> params = (Map<String, Object>) obj;
            LogUtil.e("SDK_MODULE", "SDKPrivate--uploadData()--参数拼接完成，开始上传");
            AsyncHttpRequest.requestByPost(ConnectionType.TYPE_UPLOAD, params, this, BaseUrl.UPLOAD_URL);
        } else if (type == TaskType.GET_ACCESS) {
            Access access = (Access) obj;
            if (access.getEffectTime() < System.currentTimeMillis()) {
                requestUpdateModel(getRid(), access.getTicket());
            }
        }

    }
}
