package com.sky.logPlugin.logPlugin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * LogPlugin持久化环境
 * @author: 甜筒
 * @Date: 12:13 2018/12/19
 * Modified By:
 */
@Component
public class LogPluginEnvironment{

    private static LogPluginEnvironment instance;

    @Autowired
    private DataSource dataSource;

    public DataSource getDataSource () {

        return dataSource;
    }

}
