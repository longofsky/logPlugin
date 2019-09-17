package com.sky.logPlugin.logPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *日志信息持久化 实现类工厂
 * @author: 甜筒
 * @Date: 15:16 2018/12/19
 * Modified By:
 */
public class LogPluginMinitorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPluginMinitorDBImpl.class);

    public static AbstractLogPluginMinitor getLogPluginMinitor (String durableType,LogPluginEnvironment logPluginEnvironment) {

        if("db".equals(durableType)) {
            return LogPluginMinitorDBImpl.getInstance(logPluginEnvironment);
        }

        /** 如果if内部未返回 则默认返回 LogPluginMinitorDBImpl*/
        return LogPluginMinitorDBImpl.getInstance(logPluginEnvironment);
    }

}
