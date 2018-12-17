package com.sky.mybatis.logPlugin;

/**
 * @author: 甜筒
 * @Date: 20:06 2018/12/17
 * Modified By:
 */
public class LogPluginMinitorImpl implements LogPluginMinitor {

    private LogPluginMinitorImpl () {

    }

    private static class LogPluginMinitorImplFactory {

        private static LogPluginMinitorImpl instance = new LogPluginMinitorImpl();
    }

    /** 初始化一个异步线程，并返回当前对象 todo*/
    public LogPluginMinitorImpl getInstance () {




        return LogPluginMinitorImplFactory.instance;
    }


    LogPluginContent logPluginContent  = LogPluginContent.getLogPluginContent();


    public void startExecuteLogDurableAsyn() {

    }

    public void scheduledExecuteLogDurableAsyn() {

    }

    public void executeLogDurableAsyn() {

    }

    public int executeLogDurable() {
        return 0;
    }
}
