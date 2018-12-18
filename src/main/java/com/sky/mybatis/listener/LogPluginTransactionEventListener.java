package com.sky.mybatis.listener;

import com.sky.mybatis.dao.entity.ContentScanDataEntity;
import com.sky.mybatis.enums.LogPluginDTOStatusEnums;
import com.sky.mybatis.enums.TransactionStatusEnum;
import com.sky.mybatis.logPlugin.LogPluginContent;
import com.sky.mybatis.logPlugin.LogPluginDTO;
import com.sky.mybatis.utils.ThreadUtils;
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
 * @author: 甜筒
 * @Date: 15:58 2018/12/14
 * Modified By:
 */

@Component
public class LogPluginTransactionEventListener< T extends Object> {


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit (PayloadApplicationEvent<T> event) {

        upDateLogPluginContent(event,TransactionStatusEnum.BEFORECOMMIT);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit (PayloadApplicationEvent<T> event) {

        upDateLogPluginContent(event,TransactionStatusEnum.COMMIT);
        /** 事务提交后回复线程名*/
        ThreadUtils.resetThreadNmae(Thread.currentThread());

        System.out.println("after commit, id: " );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion (PayloadApplicationEvent<T> event) {

        System.out.println("after completion, id: ");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback (PayloadApplicationEvent<T> event) {

        upDateLogPluginContent(event,TransactionStatusEnum.ROLLBACK);
        /** 事务回滚后回复线程名*/
        ThreadUtils.resetThreadNmae(Thread.currentThread());

        System.out.println("after rollback, id: " );
    }

    /** 校验sql 信息和当前事务的栈帧信息是否匹配，如果匹配更改sql信息的事务状态*/
    private void upDateLogPluginContent (PayloadApplicationEvent<T> event,TransactionStatusEnum transactionStatusEnum) {

        LogPluginEvent logPluginEvent = (LogPluginEvent) event.getPayload();

        LogPluginContent logPluginContent  = LogPluginContent.getLogPluginContent();

        /** 确认事务状态的数据从 requireTransactionVerify 转移到 waitDurable*/
        ConcurrentLinkedQueue requireTransactionVerify = logPluginContent.getRequireTransactionVerify();
        ConcurrentSkipListSet waitDurable = logPluginContent.getWaitDurable();

        if (requireTransactionVerify.isEmpty()) {
            return;
        }
        while (!requireTransactionVerify.isEmpty()) {

            LogPluginDTO logPluginDTO = (LogPluginDTO) requireTransactionVerify.poll();

            if (LogPluginDTOStatusEnums.WAITDURABLE.getIndex().equals(logPluginDTO.getLogPluginDTOStatus())) {

                logPluginContent.addWaitDurable(logPluginDTO);
                continue;
            }

            if (logPluginDTO.getStackValue().equals(logPluginEvent.getThread().getName())) {

                logPluginDTO.setCommit(transactionStatusEnum.getIndex());
                logPluginDTO.setLogPluginDTOStatus(LogPluginDTOStatusEnums.WAITDURABLE.getIndex());

                logPluginContent.addWaitDurable(logPluginDTO);
            }
        }
    }
}
