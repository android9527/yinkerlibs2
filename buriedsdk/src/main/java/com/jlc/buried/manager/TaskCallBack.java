package com.jlc.buried.manager;

/**
 * @author deliang.xie
 * @data 2016/5/28 0028
 * @time 下午 1:43
 */
public interface TaskCallBack {

    Object onTaskExecute(int type, Object... params);

    void onTaskComplete(int type, Object result);
}
