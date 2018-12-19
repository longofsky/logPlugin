package com.sky.logPlugin.enums;

import com.sky.logPlugin.logPlugin.LogPluginMinitor;
import com.sky.logPlugin.logPlugin.LogPluginMinitorDBImpl;

/**
 * @author: 甜筒
 * @Date: 15:16 2018/12/19
 * Modified By:
 */
public enum LogPluginMinitorEnums {
    DB("db","LogPluginMinitorDBImpl");

    LogPluginMinitorEnums(String name,String classImpl) {
        this.durableType = name;
        this.className = classImpl;
    }


    private String durableType;
    private String className;

    public String getDurableType() {
        return durableType;
    }

    public void setDurableType(String durableType) {
        this.durableType = durableType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "LogPluginMinitorEnums{" +
                "durableType='" + durableType + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
