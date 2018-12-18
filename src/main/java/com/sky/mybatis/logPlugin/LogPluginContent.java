package com.sky.mybatis.logPlugin;

import com.sky.mybatis.enums.LogPluginDTOStatusEnums;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: 甜筒
 * @Date: 11:20 2018/12/13
 * Modified By:
 */
public class LogPluginContent implements Serializable{

    /** 待确认事务状态集合*/
    private ConcurrentLinkedQueue<LogPluginDTO> requireTransactionVerify = new ConcurrentLinkedQueue();

    /** 带持久化集合，两个集合元素不重复*/
    private ConcurrentSkipListSet<LogPluginDTO> waitDurable = new ConcurrentSkipListSet();

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


    /** waitDurable 添加记录，失败的话回放到 requireTransactionVerify*/
    public void addWaitDurable (LogPluginDTO logPluginDTO) {

        if(!waitDurable.add(logPluginDTO)) {
            requireTransactionVerify.add(logPluginDTO);
        }
    }

    /** 操作记录添加到异步容器*/
    public void add (LogPluginDTO logPluginDTO) {

        if(LogPluginDTOStatusEnums.WAITDURABLE.getIndex().equals(logPluginDTO.getLogPluginDTOStatus())) {
            waitDurable.add(logPluginDTO);
        } else {
            requireTransactionVerify.add(logPluginDTO);
        }
    }

    public ConcurrentLinkedQueue getRequireTransactionVerify() {
        return requireTransactionVerify;
    }

    public void setRequireTransactionVerify(ConcurrentLinkedQueue requireTransactionVerify) {
        this.requireTransactionVerify = requireTransactionVerify;
    }

    public ConcurrentSkipListSet getWaitDurable() {
        return waitDurable;
    }

    public void setWaitDurable(ConcurrentSkipListSet waitDurable) {
        this.waitDurable = waitDurable;
    }

    /** toString  todo*/
}
