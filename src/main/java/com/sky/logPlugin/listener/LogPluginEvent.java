package com.sky.logPlugin.listener;


/**
 * @author: 甜筒
 * @Date: 16:15 2018/12/17
 * Modified By:
 */
public class LogPluginEvent {

    private String name;

    private Integer status;

    public LogPluginEvent(String name){

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LogPluginEvent{" +
                "name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
