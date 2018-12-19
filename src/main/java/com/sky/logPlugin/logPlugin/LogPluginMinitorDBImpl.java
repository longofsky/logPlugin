package com.sky.logPlugin.logPlugin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sky.logPlugin.annotation.AddLogPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.applet.AppletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author: 甜筒
 * @Date: 20:06 2018/12/17
 * Modified By:
 */
public class LogPluginMinitorDBImpl extends AbstractLogPluginMinitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPluginMinitorDBImpl.class);


    /** 确保当前对象为单例*/
    private LogPluginMinitorDBImpl () {

    }

    private static class LogPluginMinitorImplFactory {

        private static LogPluginMinitorDBImpl instance = new LogPluginMinitorDBImpl();
    }

    /** 初始化一个实例，并返回当前对象 */
    public static LogPluginMinitorDBImpl getInstance (LogPluginEnvironment logPluginEnvironment) {

        LogPluginMinitorDBImpl instance  = LogPluginMinitorImplFactory.instance;
        instance.logPluginEnvironment = logPluginEnvironment;
        instance.scheduledExecuteLogDurableAsyn();

        return instance;
    }

    @Override
    public Integer executeLogDurable(List<LogPluginDTO> logPluginDTOS) {

        LOGGER.info(logPluginDTOS.toString());

        DataSource dataSource = logPluginEnvironment.getDataSource();
        try {
            /** 如果还是没有拿到dataSource*/
            if (dataSource == null) {
                LOGGER.info("线程："+Thread.currentThread().getName()+"未获取到任何数据源，因此不执行");
                return -1;
            }
            LOGGER.info("成功获取数据源");
            /** 模板类对象*/
            JdbcTemplate template = new JdbcTemplate(dataSource);
            /** 添加语句 todo*/
            String sql = "insert into tableTest (value1,value2) values(?,?)";
//            /** 执行操作*/
//            template.batchUpdate(sql, new BatchPreparedStatementSetter() {
//
//                public void setValues(PreparedStatement pstat, int i)
//                        throws SQLException {
//                    LogPluginDTO logPluginDTO = logPluginDTOS.get(i);
//                    /** 组装 PreparedStatement*/
//                    pstat.setString(1,null);
//                }
//
//                public int getBatchSize() {
//                    /** 设置本批次一共多少组数据，隐含的就是循环几次*/
//                    return logPluginDTOS.size();
//                }
//            });
        } catch (Throwable e) {
            LOGGER.error("执行Sql index异常  ", e);
        }
        return 1;
    }
}
