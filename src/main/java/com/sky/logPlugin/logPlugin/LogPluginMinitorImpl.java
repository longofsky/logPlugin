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
public class LogPluginMinitorImpl implements LogPluginMinitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPluginMinitorImpl.class);

    private DataSource dataSource = null;

    /**
     * 待执行队列中超过此值测执行异步持久化线程
     */
    private static final int DEFAULT_MONITOR_LOGPLUGIN_QUEUE_SIZE = 500;

    private static final int MAP_INIT_SIZE = 16;

    /**
     * 待执行队列中超过此值测执行异步持久化线程
     */
    private int monitorLogPluginQueueMaxSize = DEFAULT_MONITOR_LOGPLUGIN_QUEUE_SIZE;

    /**
     * 执行 日志持久化的时间间隔 （秒）
     */
    private Integer logPluginExecuteIntervalSeconds = 5;

    /**
     * 是否正在执行 waitDurable 的队列
     */
    private volatile boolean monitorLogPluginQueueRunning = false;


    LogPluginContent logPluginContent  = LogPluginContent.getLogPluginContent();

    private Map<String, DataSource> dataSourceMap;

    /**
     * 优先经高于 dataSourceMap 的数据源缓存
     */
    private DataSource defaultDataSource;

    private ScheduledExecutorService scheduledExecutorService;


    private LogPluginMinitorImpl () {

    }

    private static class LogPluginMinitorImplFactory {

        private static LogPluginMinitorImpl instance = new LogPluginMinitorImpl();
    }

    /** 初始化一个异步线程，并返回当前对象 */
    public static LogPluginMinitorImpl getInstance (DataSource dataSource) {

        LogPluginMinitorImpl instance  = LogPluginMinitorImplFactory.instance;

        instance.dataSource = dataSource;
        instance.scheduledExecuteLogDurableAsyn();

        return instance;
    }


    public void startExecuteLogDurableAsyn() {

        if (logPluginContent.getWaitDurable().size() >= monitorLogPluginQueueMaxSize) {
            executeLogDurableAsyn();
        }
    }

    public void scheduledExecuteLogDurableAsyn() {

        if (scheduledExecutorService != null) {
            return;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("LogPluginMinitorImpl-pool-%d").build();
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        LOGGER.info("LogPluginMinitorImpl logPlugin定时器 scheduledExecutorService启动 {}", scheduledExecutorService);
        /** 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间*/
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        executeLogDurable();
                    }
                }
                , logPluginExecuteIntervalSeconds, logPluginExecuteIntervalSeconds, TimeUnit.SECONDS);
    }

    public void executeLogDurableAsyn() {
        if (!monitorLogPluginQueueRunning) {
            scheduledExecutorService.execute(new Runnable() {
                public void run() {
                    executeLogDurable();
                }
            });
        }
    }

    /** 此方法保证单线程执行*/
    public int executeLogDurable() {
        if (monitorLogPluginQueueRunning) {
            return -1;
        }
        monitorLogPluginQueueRunning = true;
        int executedCount = 0;
        try {

            long curtimeStart = System.currentTimeMillis();
            List<Map<String, Object>> sqlLogs = new ArrayList();

            List<LogPluginDTO> logPluginDTOs = new ArrayList<LogPluginDTO>(DEFAULT_MONITOR_LOGPLUGIN_QUEUE_SIZE);

            while (!logPluginContent.getWaitDurable().isEmpty() && logPluginDTOs.size() <= DEFAULT_MONITOR_LOGPLUGIN_QUEUE_SIZE) {

                /** 添加 带批量处理集合 并删除元数据*/
                logPluginDTOs.add((LogPluginDTO)logPluginContent.getWaitDurable().pollFirst());

            }
            /** 批量插入*/
            if (logPluginDTOs.isEmpty()) {

                return executedCount;
            }
            executeLogDurableDB(logPluginDTOs);

        } catch (Exception e) {
            LOGGER.info("执行logPlugin  队列异常", e);
        } finally {
            /** 将monitorExplianQueueRunning 置成true*/
            monitorLogPluginQueueRunning = false;
        }
        return executedCount;
    }

    private boolean executeLogDurableDB(final List<LogPluginDTO> logPluginDTOS) {

        LOGGER.info(logPluginDTOS.toString());

        try {
            /** 如果还是没有拿到dataSource*/
            if (dataSource == null) {
                LOGGER.info("未获取到任何数据源，因此不执行");
                return false;
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
        } catch (Exception e) {
            LOGGER.warn("执行Sql index异常  ", e);
        }
        return false;
    }
}
