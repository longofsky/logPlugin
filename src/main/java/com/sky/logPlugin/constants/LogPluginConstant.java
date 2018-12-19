package com.sky.logPlugin.constants;

/**
 * @author: 甜筒
 * @Date: 10:41 2018/12/19
 * Modified By:
 */
public class LogPluginConstant {

    /** 是否开启声明式事务标识*/
    public static final String TRANSACTIONAL_ANNOTATION_FLAG = "transactionalAnnotationFlag";

    /** 单个任务进入 待确认事务状态队列次数*/
    public static final Integer LOGPLUGINDTO_DO_TRANSACTIONAL_MAX = 10;

    /**
     * 待持久化队列中超过此值-执行异步持久化线程
     */
    public static final int DEFAULT_MONITOR_LOGPLUGIN_QUEUE_SIZE = 500;
    /**
     * 单次持久化的最大值
     */
    public static final int DEFAULT_WAITDURABLE_QUEUE_MAX_SIZE = 500;
}
