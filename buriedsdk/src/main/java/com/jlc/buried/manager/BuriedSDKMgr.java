package com.jlc.buried.manager;

import android.content.Context;

/**
 * 数据埋点管理类
 *
 * @author deliang.xie
 * @data 2016/5/20 0020
 * @time 下午 2:41
 */
public class BuriedSDKMgr {
    private volatile static BuriedSDKMgr mSdkMgr;

    private SDKMgrPrivate sdkMgrPrivate;

    private BuriedSDKMgr(Context application) {
        sdkMgrPrivate = new SDKMgrPrivate(application);
    }

    public static BuriedSDKMgr getInstance(Context application) {
        if (null == mSdkMgr) {
            synchronized (BuriedSDKMgr.class) {
                if (null == mSdkMgr) {
                    mSdkMgr = new BuriedSDKMgr(application);
                }
            }
        }
        return mSdkMgr;
    }

    /**
     * 初始化
     *
     * @param appKey
     */
    public void init(String appKey) {
        sdkMgrPrivate.init(appKey);
    }

    /**
     * 上传数据
     */
    public void uploadModule() {
        sdkMgrPrivate.uploadData();
    }

    public void saveModule(String key, String moduleStr) {
        sdkMgrPrivate.saveModule(key, moduleStr);
    }

}
