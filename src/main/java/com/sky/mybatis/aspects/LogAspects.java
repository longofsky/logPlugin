package com.sky.mybatis.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

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

        Signature signature = joinPoint.getSignature();
        String calssname = signature.getDeclaringTypeName();
        String methor =  signature.getName();

        Thread thread = Thread.currentThread();
        thread.setName(calssname+"."+methor+":"+thread.getName());
        publisher.publishEvent(thread.toString());

        Object[] args = joinPoint.getArgs();
        System.out.println(""+joinPoint.getSignature().getName()+"运行。。。@Before:参数列表是：{"+ Arrays.asList(args)+"}");
    }

    @After("pointCut()")
    public void logPluginEnd (JoinPoint joinPoint ) {
        System.out.println(""+joinPoint.getSignature().getName()+"结束。。。@After");
    }

    @AfterReturning(value = "pointCut()",returning = "result")
    public void logPluginReturn (JoinPoint joinPoint ) {
        System.out.println(""+joinPoint.getSignature().getName()+"返回。。。@AfterReturning");
    }

    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logPluginException (JoinPoint joinPoint ) {
        System.out.println(""+joinPoint.getSignature().getName()+"抛出异常。。。@AfterThrowing");
    }

}
