package com.sky.logPlugin.logPlugin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author: 甜筒
 * @Date: 19:51 2018/12/17
 * Modified By:
 */
public abstract class AbstractLogPluginMinitor implements LogPluginMinitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLogPluginMinitor.class);

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
    private Integer logPluginExecuteIntervalSeconds = 20;

    /**
     * 是否正在执行 waitDurable 的队列
     */
    private volatile boolean monitorLogPluginQueueRunning = false;

    /** 持久化环境*/
    public LogPluginEnvironment logPluginEnvironment;

    LogPluginContent logPluginContent  = LogPluginContent.getLogPluginContent();

    private ScheduledExecutorService scheduledExecutorService;

    public void startExecuteLogDurableAsyn() {

        if (logPluginContent.getWaitDurable().size() >= monitorLogPluginQueueMaxSize) {
            executeLogDurableAsyn();
        }
    }

    public void scheduledExecuteLogDurableAsyn() {

        if (scheduledExecutorService != null) {
            return;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("LogPluginMinitorDBImpl-pool-%d").build();
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        LOGGER.info("LogPluginMinitorDBImpl logPlugin定时器 scheduledExecutorService启动 {}", scheduledExecutorService);
        /** 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间*/
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        transformExecuteLogDurable();
                    }
                }
                , logPluginExecuteIntervalSeconds, logPluginExecuteIntervalSeconds, TimeUnit.SECONDS);
    }

    public void executeLogDurableAsyn() {
        if (!monitorLogPluginQueueRunning) {
            scheduledExecutorService.execute(new Runnable() {
                public void run() {
                    transformExecuteLogDurable();
                }
            });
        }
    }

    /** 此方法保证单线程执行*/
    public int transformExecuteLogDurable() {
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
            executeLogDurable(logPluginDTOs);

        } catch (Exception e) {
            LOGGER.info("执行logPlugin  队列异常", e);
        } finally {
            /** 将monitorExplianQueueRunning 置成true*/
            monitorLogPluginQueueRunning = false;
        }
        return executedCount;
    }

    public Boolean executeLogDurable(final List<LogPluginDTO> logPluginDTOS) {

        return false;
    }
}
