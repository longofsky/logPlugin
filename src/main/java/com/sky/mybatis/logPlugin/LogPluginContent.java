package com.sky.mybatis.logPlugin;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 甜筒
 * @Date: 11:20 2018/12/13
 * Modified By:
 */
public class LogPluginContent implements Serializable{

    /** 系统内执行sql 执行记录 key=code + classPath + sqlMethor + sql + param*/
    private ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    /** 持有私静态实例，防止被引用此处赋值为 持有私静态实例，防止被引用此处赋值为 null ，目的是实现延迟加载 */
    private static  LogPluginContent logPluginContent = null;

    /** 私有构造方法，防止被实例化*/
    private LogPluginContent () {
    }

    /** 此处引入一个内部对象来维护单例*/
    private static class LogPluginContentFactory {

        private static LogPluginContent instance = new LogPluginContent();
    }

    public static LogPluginContent getLogPluginContent () {

        return LogPluginContentFactory.instance;
    }


    /** 操作记录添加到异步容器*/
    public void add (LogPluginDTO logPluginDTO) {

        concurrentHashMap.put(logPluginDTO.getKey(),logPluginDTO);
    }
    /** 操作记录异步持久化并删除*/

    public LogPluginDTO add (String key) {

        Object o = concurrentHashMap.remove(key);
        if (o == null) {

            return null;
        }
        return (LogPluginDTO)o;
    }

    public ConcurrentHashMap getConcurrentHashMap() {
        return concurrentHashMap;
    }

    public void setConcurrentHashMap(ConcurrentHashMap concurrentHashMap) {
        this.concurrentHashMap = concurrentHashMap;
    }

    @Override
    public String toString() {
        return "LogPluginContent{" +
                "concurrentHashMap=" + concurrentHashMap +
                '}';
    }
}
