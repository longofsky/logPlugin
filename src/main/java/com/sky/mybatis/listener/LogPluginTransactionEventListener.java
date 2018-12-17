package com.sky.mybatis.listener;

import com.sky.mybatis.dao.entity.ContentScanDataEntity;
import com.sky.mybatis.enums.TransactionStatusEnum;
import com.sky.mybatis.logPlugin.LogPluginContent;
import com.sky.mybatis.logPlugin.LogPluginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        System.out.println("after commit, id: " );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion (PayloadApplicationEvent<T> event) {

//        upDateLogPluginContent(event,TransactionStatusEnum.COMPLETION);
        System.out.println("after completion, id: ");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback (PayloadApplicationEvent<T> event) {

        upDateLogPluginContent(event,TransactionStatusEnum.ROLLBACK);
        System.out.println("after rollback, id: " );
    }

    private void upDateLogPluginContent (PayloadApplicationEvent<T> event,TransactionStatusEnum transactionStatusEnum) {

        String s = (String) event.getPayload();

        LogPluginContent logPluginContent  = LogPluginContent.getLogPluginContent();

        ConcurrentHashMap concurrentHashMap = logPluginContent.getConcurrentHashMap();

        if (concurrentHashMap.size() > 0) {

            for ( Object o: concurrentHashMap.keySet() ) {
                LogPluginDTO logPluginDTO = (LogPluginDTO)concurrentHashMap.get(o);

                if (logPluginDTO.getValue() != null && logPluginDTO.getValue().equals(s)) {

                    logPluginDTO.setCommit(transactionStatusEnum.getDesc());
                }
            }
        }

    }

}
