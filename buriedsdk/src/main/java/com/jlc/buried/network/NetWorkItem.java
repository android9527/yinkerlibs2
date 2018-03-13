package com.jlc.buried.network;

import com.jlc.buried.config.Config;
import com.jlc.buried.network.model.ResponseData;
import com.jlc.buried.utils.JsonUtil;
import com.jlc.buried.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author deliang.xie
 * @data 2016/5/26 0026
 * @time 下午 3:34
 */
public class NetWorkItem implements Runnable {

    private Map<String, Object> map;
    private String requestUrl;
    private int sendType;//GET/POST
    private int type;

    private WeakReference<NetWorkCallBack> callBack;

    {
        map = new HashMap<>();
    }

    public NetWorkItem(int sendType, String requestUrl, int type, Map<String, Object> map, NetWorkCallBack callBack) {
        this.type = type;
        this.sendType = sendType;
        this.map = map;
        this.callBack = new WeakReference<>(callBack);
        this.requestUrl = requestUrl;

    }

    @Override
    public void run() {
        switch (sendType) {
            case AsyncHttpRequest.POST: {
                doHttpPostRequest();
                break;
            }
            case AsyncHttpRequest.GET: {

                break;
            }
            default: {
                break;
            }
        }


    }

    /**
     * httpPost
     */
    private void doHttpPostRequest() {
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer params = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        // 组织请求参数
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            params.append(element.getKey());
            params.append("=");
            params.append(element.getValue());
            params.append("&");
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }

        try {
            URL realUrl = new URL(requestUrl);
            // 打开和URL之间的连接
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();
            // 设置超时
            httpURLConnection.setConnectTimeout(Config.REQUEST_TIMEOUT);
            httpURLConnection.setReadTimeout(Config.REQUEST_TIMEOUT);
            // 设置通用的请求属性
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");   //设置POST方式连接
            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(params.toString());
            // flush输出流的缓冲
            printWriter.flush();
            // 定义BufferedReader输入流来读取URL的ResponseData
            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));//设置编码,否则中文乱码
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            // 根据ResponseCode判断连接是否成功
            int responseCode = httpURLConnection.getResponseCode();
            if (callBack != null) {
                NetWorkCallBack back = callBack.get();
                if (back != null) {
                    if (responseCode != 200) {
                        back.onNetWorkError(sb.toString());
                    } else {
                        handlerNetWorkSuccess(sb.toString());
                    }
                }
            }
        } catch (Exception e) {
            if (callBack != null) {
                NetWorkCallBack back = callBack.get();
                if (back != null) {
                    back.onNetWorkError(e.toString());
                }
            }
        } finally {
            httpURLConnection.disconnect();
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void handlerNetWorkSuccess(String jsonStr) {
        LogUtil.e("SDK_MODULE", "NetWork--uploadData()--网络回掉成功");

        Map<String, Object> resultMap = JsonUtil.json2Map(jsonStr);
        if (resultMap != null) {
            ResponseData data = new ResponseData();
            data.setConnectionType(type);
            if (resultMap.get("status") != null) {
                double d = (Double) resultMap.get("status");
                data.status = (int) d;
            }

            if (resultMap.get("desc") != null) {
                data.desc = (String) resultMap.get("desc");
            }

            if (resultMap.get("rid") != null) {
                double d = (double) resultMap.get("rid");
                data.rid = (int) d;
            }

            if (resultMap.get("version") != null) {
                double d = (double) resultMap.get("version");
                data.version = (float) d;
            }

            if (resultMap.get("result") != null) {
                data.setResponseData(JsonUtil.object2Json(resultMap.get("result")));
            }
            if (callBack != null) {
                NetWorkCallBack back = callBack.get();
                if (back != null) {
                    back.onResponse(data);
                }
            }
        }
    }
}
