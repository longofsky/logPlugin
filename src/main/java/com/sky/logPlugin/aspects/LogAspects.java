package com.sky.logPlugin.aspects;

import com.sky.logPlugin.constants.LogPluginConstant;
import com.sky.logPlugin.listener.LogPluginEvent;
import com.sky.logPlugin.utils.ThreadUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author: 甜筒
 * @Date: 14:02 2018/12/17
 * Modified By:
 *
 * 配合LogExecutorPlugin 使用的切面类
 * 功能：当方法上注解事务时，开启事务的事件通知，关联LogExecutorPlugin逻辑
 *
 */
@Component
@Aspect
public class LogAspects {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void pointCut () {}

    @Before("pointCut()")
    public void logPluginStart (JoinPoint joinPoint ) {

        /** 获取被拦截方法信息*/
         Signature signature = joinPoint.getSignature();
        String calssname = signature.getDeclaringTypeName();
        String methor =  signature.getName();

        /** 重新组装当前线程的name，组装规则：“当前线程名称：类的全限定名.方法名：当前系统时间毫秒值”*/
        Thread thread = Thread.currentThread();
        thread.setName(thread.getName()+":"+LogPluginConstant.TRANSACTIONAL_ANNOTATION_FLAG+":"+calssname+"."+methor+":"+ UUID.randomUUID()+":"+System.currentTimeMillis());

        /** 封装日志插件类型发布事件*/
        publisher.publishEvent(new LogPluginEvent(thread.getName()));

    }

}
