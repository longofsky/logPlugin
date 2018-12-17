package com.sky.mybatis.aspects;

import com.sky.mybatis.listener.LogPluginEvent;
import com.sky.mybatis.utils.ThreadUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author: 甜筒
 * @Date: 14:02 2018/12/17
 * Modified By:
 *
 * 配合LogExecutorPlugin 使用的切面类
 * 功能：当方法上注解事务时，开启事务的事件通知，关联LogExecutorPlugin逻辑
 *
 */
@Aspect
public class LogAspects {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Pointcut("execution(public * com.sky.mybatis.dao.impl.ContentScanDataServiceImpl.*(..))")
    public void pointCut () {}

    @Before("pointCut()")
    public void logPluginStart (JoinPoint joinPoint ) {

        /** 获取被拦截方法信息*/
        Signature signature = joinPoint.getSignature();
        String calssname = signature.getDeclaringTypeName();
        String methor =  signature.getName();


        /** 重新组装当前线程的name，组装规则：“当前线程名称：类的全限定名.方法名：当前系统时间毫秒值”*/
        Thread thread = Thread.currentThread();
        thread.setName(thread.getName()+":"+calssname+"."+methor+":"+System.currentTimeMillis());

        /** 校验是否标识了声明式事务，如果未标识则不执行 事务事件发布逻辑 todo*/

        /** 封装日志插件类型发布事件*/
        publisher.publishEvent(new LogPluginEvent(thread));

    }

}
