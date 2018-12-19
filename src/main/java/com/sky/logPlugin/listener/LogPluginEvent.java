package com.sky.logPlugin.listener;

import com.alibaba.fastjson.JSON;

/**
 * @author: 甜筒
 * @Date: 16:15 2018/12/17
 * Modified By:
 */
public class LogPluginEvent {

    private Thread thread;

    public LogPluginEvent(Thread thread) {

        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
