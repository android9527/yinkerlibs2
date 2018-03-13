package com.jlc.buried.manager;

import java.lang.ref.WeakReference;

/**
 * 读写数据库使用
 *
 * @author deliang.xie
 * @data 2016/5/28 0028
 * @time 下午 12:26
 */
public class TaskItem implements Runnable {


    private WeakReference<TaskCallBack> callBack;
    private int taskType;
    private Object[] params;

    public TaskItem(int taskType, TaskCallBack callBack) {
        this.taskType = taskType;
        this.callBack = new WeakReference<>(callBack);
    }

    public TaskItem(int taskType, TaskCallBack callBack, Object... params) {
        this.params = params;
        this.taskType = taskType;
        this.callBack = new WeakReference<>(callBack);
    }


    @Override
    public void run() {
        TaskCallBack back = callBack.get();
        if (back != null) {
            Object result = back.onTaskExecute(taskType, params);
            back.onTaskComplete(taskType, result);
        }
    }

}
