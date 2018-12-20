package com.sky.logPlugin.logPlugin;

import com.sky.logPlugin.constants.LogPluginConstant;
import com.sky.logPlugin.enums.LogPluginDTOStatusEnums;
import com.sky.logPlugin.enums.TransactionStatusEnum;
import com.sky.logPlugin.listener.LogPluginEvent;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 甜筒
 * @Date: 11:20 2018/12/13
 * Modified By:
 */

public class LogPluginContent implements Serializable{

    /** 待确认事务状态集合*/
    private ConcurrentLinkedQueue<LogPluginDTO> requireTransactionVerify = new ConcurrentLinkedQueue();


    /** 保存事务提交的事件*/
    private ConcurrentHashMap<String,LogPluginEvent> LogPluginEvent = new ConcurrentHashMap();

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
            this.addRequireTransactionVerify(logPluginDTO);
        }
    }
    /** requireTransactionVerify添加数据*/
    public void addRequireTransactionVerify (LogPluginDTO logPluginDTO) {

        /** 当 logPluginDTO 进入requireTransactionVerify 的次数达到 LOGPLUGINDTO_DO_TRANSACTIONAL_MAX 时，默认事务状态更新失败*/
        /** 此段代码不需要考虑 同步，atomicInteger 的大小不做严格要求*/
        if (logPluginDTO.getAtomicInteger().get() >= LogPluginConstant.LOGPLUGINDTO_DO_TRANSACTIONAL_MAX) {
            logPluginDTO.setCommit(TransactionStatusEnum.TRANSACTIONFAIL.getIndex());
            logPluginDTO.setLogPluginDTOStatus(LogPluginDTOStatusEnums.WAITDURABLE.getIndex());
            waitDurable.add(logPluginDTO);
        }

        logPluginDTO.getAtomicInteger().incrementAndGet();
        requireTransactionVerify.add(logPluginDTO);

    }

    /** 操作记录添加到异步容器*/
    public void add (LogPluginDTO logPluginDTO) {

        if(LogPluginDTOStatusEnums.WAITDURABLE.getIndex().equals(logPluginDTO.getLogPluginDTOStatus())) {
            this.addWaitDurable(logPluginDTO);
        } else {
            this.addRequireTransactionVerify(logPluginDTO);
        }
    }

    public ConcurrentLinkedQueue getRequireTransactionVerify() {
        return requireTransactionVerify;
    }

    public ConcurrentSkipListSet getWaitDurable() {
        return waitDurable;
    }

    public ConcurrentHashMap<String,LogPluginEvent> getLogPluginEvent() {
        return LogPluginEvent;
    }

    @Override
    public String toString() {
        return "LogPluginContent{" +
                "requireTransactionVerify=" + requireTransactionVerify +
                ", LogPluginEvent=" + LogPluginEvent +
                ", waitDurable=" + waitDurable +
                '}';
    }
}
