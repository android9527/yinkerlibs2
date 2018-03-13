package com.jlc.buried.network;

import com.jlc.buried.utils.ThreadPoolUtils;

import java.util.Map;

/**
 * 异步数据请求
 *
 * @author chen
 * @date 2012-10-25 下午2:57:33
 */
public class AsyncHttpRequest {

    static final int POST = 1; // post 提交
    static final int GET = 2; // get 提交

    /**
     * 异步请求网络数据
     *
     * @param sendType 请求类型
     * @param callBack 回调对象
     * @param params   参数
     * @param url      请求URL
     */
    private static void doAsyncRequest(final int sendType, final int type,
                                       final Map<String, Object> params, final NetWorkCallBack callBack, final String url) {
        // 请求
        ThreadPoolUtils.execute(new NetWorkItem(sendType, url, type, params, callBack));
    }

    /**
     * 访问网络初始化函数 支持Post请求方式
     *
     * @param type     请求Type
     * @param callBack 回调执行函数 支持线程
     * @param url      每个执行url
     * @param params   参数
     */
    public static void requestByPost(final int type,
                                     final Map<String, Object> params,
                                     final NetWorkCallBack callBack, final String url) {
        // 异步请求数据
        doAsyncRequest(POST, type, params, callBack, url);
    }
}
