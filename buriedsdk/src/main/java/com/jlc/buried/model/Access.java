package com.jlc.buried.model;

import android.text.TextUtils;

import com.jlc.buried.utils.JsonUtil;
import com.jlc.buried.utils.LogUtil;

import java.util.Map;

/**
 * 鉴权数据
 *
 * @author deliang.xie
 * @data 2016/5/19 0019
 * @time 下午 3:49
 */
public class Access {

    private static final String TAG = Access.class.getSimpleName();
    private String mTicket;
    private long mEffectTime;
    private String mModuleStatus;

    public String getTicket() {
        return mTicket;
    }

    public void setTicket(String mTicket) {
        this.mTicket = mTicket;
    }

    public long getEffectTime() {
        return mEffectTime;
    }

    public void setEffectTime(long mEffectTime) {
        this.mEffectTime = mEffectTime;
    }


    public String getModuleStatus() {
        return mModuleStatus;
    }

    public void setModuleStatus(String mModuleStatus) {
        this.mModuleStatus = mModuleStatus;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mModuleStatus);
    }

    public void parseData(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }
        try {
            Map<String, Object> jsonMap = JsonUtil.json2Map(jsonStr);
            if (jsonMap.get("ticket") != null) {
                this.mTicket = (String) jsonMap.get("ticket");
            }

            if (jsonMap.get("effectTime") != null) {
                double d = (double) jsonMap.get("effectTime");
                this.mEffectTime = (long) d;
            }

            if (jsonMap.get("module") != null) {
                this.mModuleStatus = JsonUtil.object2Json(jsonMap.get("module"));
            }

        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }
}
