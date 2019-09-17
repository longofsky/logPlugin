package com.sky.logPlugin.listener;

import com.sky.logPlugin.dao.entity.ContentScanDataEntity;
import com.sky.logPlugin.enums.LogPluginDTOStatusEnums;
import com.sky.logPlugin.enums.TransactionStatusEnum;
import com.sky.logPlugin.logPlugin.LogPluginContent;
import com.sky.logPlugin.logPlugin.LogPluginDTO;
import com.sky.logPlugin.logPlugin.LogPluginMinitorDBImpl;
import com.sky.logPlugin.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *日志中间件事务事件监听类
 * @author: 甜筒
 * @Date: 15:58 2018/12/14
 * Modified By:
 */

@Component
public class LogPluginTransactionEventListener< T extends Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPluginMinitorDBImpl.class);


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit (PayloadApplicationEvent<T> event) {

        LOGGER.info("before commit:"+((LogPluginEvent)event.getPayload()).getName());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit (PayloadApplicationEvent<T> event) {

        upDateLogPluginContent(event,TransactionStatusEnum.COMMIT);
        /** 事务提交后回复线程名*/
        ThreadUtils.resetThreadNmae(Thread.currentThread());

        System.out.println("after commit:" +((LogPluginEvent)event.getPayload()).getName());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion (PayloadApplicationEvent<T> event) {

        System.out.println("after completion: "+((LogPluginEvent)event.getPayload()).getName());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback (PayloadApplicationEvent<T> event) {

        upDateLogPluginContent(event,TransactionStatusEnum.ROLLBACK);
        /** 事务回滚后重置线程名*/
        ThreadUtils.resetThreadNmae(Thread.currentThread());

        System.out.println("after rollback: " +((LogPluginEvent)event.getPayload()).getName());
    }

    /** 校验sql 信息和当前事务的栈帧信息是否匹配，如果匹配更改sql信息的事务状态--需要加锁--为了不影响主线逻辑性能这里的逻辑调整为异步处理*/
    private void upDateLogPluginContent (PayloadApplicationEvent<T> event,TransactionStatusEnum transactionStatusEnum) {

        LogPluginEvent logPluginEvent = (LogPluginEvent) event.getPayload();
        logPluginEvent.setStatus(transactionStatusEnum.getIndex());

        LogPluginContent logPluginContent  = LogPluginContent.getLogPluginContent();

        logPluginContent.getLogPluginEvent().put(logPluginEvent.getName(),logPluginEvent);

    }
}
