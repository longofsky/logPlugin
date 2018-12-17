package com.sky.mybatis.logPlugin;

/**
 * @author: 甜筒
 * @Date: 19:51 2018/12/17
 * Modified By:
 */
public interface LogPluginMinitor {

    /**
     *试探性的调用异步持久化sql记录-并不一定真的调用，
     * @Autowired: 甜筒
     * 
     * @Date:
     * @Modified By:
     */
    public void startExecuteLogDurableAsyn ();

    /**
     *周期性异步持久化sql调用记录
     * @Autowired: 甜筒
     * 
     * @Date:
     * @Modified By:
     */
    public void scheduledExecuteLogDurableAsyn ();

    /**
     *异步持久化sql调用记录
     * @Autowired: 甜筒
     * 
     * @Date:
     * @Modified By:
     */
    public void executeLogDurableAsyn ();
    
    /**
     *同步持久化sql调用记录
     * @Autowired: 甜筒
     * 
     * @Date:
     * @Modified By:
     */
    public int executeLogDurable ();

}
