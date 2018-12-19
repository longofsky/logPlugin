package com.sky.logPlugin.logPlugin;

import java.util.List;

/**
 * @author: 甜筒
 * @Date: 19:51 2018/12/17
 * Modified By:
 */
public interface LogPluginMinitor {

    /**
     *试探性的调用异步持久化sql记录-并不一定真的调用，
     * @Autowired: 甜筒
     * @Date:
     * @Modified By:
     */
    public void startExecuteLogDurableAsyn ();

    /**
     *周期性异步持久化sql调用记录
     * @Autowired: 甜筒
     * @Date:
     * @Modified By:
     */
    public void scheduledExecuteLogDurableAsyn ();

    /**
     *异步持久化sql调用记录
     * @Autowired: 甜筒
     * @Date:
     * @Modified By:
     */
    public void executeLogDurableAsyn ();

    /**
     *转换带持久化数据
     * @Autowired: 甜筒
     * @Date:
     * @Modified By:
     */
    public int transformExecuteLogDurable ();
    
    /**
     *同步持久化sql调用记录-吃方法允许重写 实现不同的持久化方式
     * @Autowired: 甜筒
     * @Date:
     * @param logPluginDTOS
     * @Modified By:
     */
    public Integer executeLogDurable (final List<LogPluginDTO> logPluginDTOS);

}
