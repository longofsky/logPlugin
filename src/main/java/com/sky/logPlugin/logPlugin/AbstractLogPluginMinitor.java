package com.sky.logPlugin.logPlugin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sky.logPlugin.constants.LogPluginConstant;
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

    private static final int MAP_INIT_SIZE = 16;

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

        if (logPluginContent.getWaitDurable().size() >= LogPluginConstant.DEFAULT_MONITOR_LOGPLUGIN_QUEUE_SIZE) {
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

        Long startTime = System.currentTimeMillis();


        if (monitorLogPluginQueueRunning) {

            LOGGER.info("线程："+Thread.currentThread().getName()+"运行时间："+(System.currentTimeMillis()-startTime)+"结束点：monitorLogPluginQueueRunning=true");
            return -1;

        }
        monitorLogPluginQueueRunning = true;
        int executedCount = 0;
        int logPluginDTOSize = 0;
        try {

            long curtimeStart = System.currentTimeMillis();
            List<Map<String, Object>> sqlLogs = new ArrayList();

            List<LogPluginDTO> logPluginDTOs = new ArrayList<LogPluginDTO>(LogPluginConstant.DEFAULT_WAITDURABLE_QUEUE_MAX_SIZE);

            while (!logPluginContent.getWaitDurable().isEmpty() && logPluginDTOs.size() <= LogPluginConstant.DEFAULT_WAITDURABLE_QUEUE_MAX_SIZE) {

                /** 添加 带批量处理集合 并删除元数据*/
                logPluginDTOs.add((LogPluginDTO)logPluginContent.getWaitDurable().pollFirst());

            }
            /** 批量插入*/
            if (logPluginDTOs.isEmpty()) {
                LOGGER.info("线程："+Thread.currentThread().getName()+"运行时间："+(System.currentTimeMillis()-startTime)+"结束点：logPluginDTOs 为空");

                return -1;
            }
            logPluginDTOSize = logPluginDTOs.size();
            executeLogDurable(logPluginDTOs);

        } catch (Exception e) {
            LOGGER.error("执行logPlugin  队列异常", e);
        } finally {
            /** 将monitorExplianQueueRunning 置成true*/
            monitorLogPluginQueueRunning = false;
        }
        LOGGER.info("线程："+Thread.currentThread().getName()+" 运行时间："+(System.currentTimeMillis()-startTime)+" 结束点：正常执行完毕。 元素："+logPluginDTOSize+"个。 执行："+executedCount+"个");

        return executedCount;
    }

    public Integer executeLogDurable(final List<LogPluginDTO> logPluginDTOS) {

        return -1;
    }
}
