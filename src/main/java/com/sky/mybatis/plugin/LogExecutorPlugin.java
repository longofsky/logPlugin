package com.sky.mybatis.plugin;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.sky.mybatis.annotation.AddLogPlugin;
import com.sky.mybatis.logPlugin.LogPluginContent;
import com.sky.mybatis.logPlugin.LogPluginDTO;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AdvisorChainFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;
import java.util.Properties;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class, CacheKey.class,BoundSql.class}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
        }
)
public class LogExecutorPlugin implements Interceptor {


    @Autowired (required = false)
    private AdvisedSupport advised;
    @Autowired (required = false)
    private AdvisorChainFactory advisorChainFactory;
    @Autowired (required = false)
    private TransactionInterceptor transactionInterceptor;

    Properties properties = null;

    private LogPluginContent logPluginContent = LogPluginContent.getLogPluginContent();

    private static final Integer MAPPED_STATEMENT_INDEX = 0;
    private static final Integer PARAMETER_INDEX = 1;
    private static final Integer ROW_BOUNDS_INDEX = 2;



    public Object intercept(Invocation invocation) throws Throwable {

        TransactionAttributeSource transactionAttributeSource = transactionInterceptor.getTransactionAttributeSource();

        Class aClass = Class.forName("com.sky.mybatis.dao.impl.ContentScanDataServiceImpl");

        Method method = aClass.getMethod("updateAppKeyByid",Long.class,String.class);

        TransactionAttribute transactionAttribute = transactionAttributeSource.getTransactionAttribute(method,aClass);

        PlatformTransactionManager platformTransactionManager = transactionInterceptor.getTransactionManager();

        System.out.println(transactionInterceptor.hashCode());

        System.out.println(invocation.toString());

        this.addLogPluginContent(invocation);

        return invocation.proceed();
    }

    public Object plugin(Object target) {

        return Plugin.wrap(target, this);

    }

    public void setProperties(Properties properties) {

        this.properties = properties;
    }

    private void addLogPluginContent(Invocation invocation) {

        LogPluginDTO logPluginDTO = this.invocationToLogPluginDTO(invocation);

        /** 获取当前方法上的注解信息*/
        Class c = null;
        try {
            c = Class.forName(logPluginDTO.getClassPath());

            AddLogPlugin annotationForClass = (AddLogPlugin)c.getAnnotation(AddLogPlugin.class);

            if (annotationForClass != null) {

                logPluginDTO.setValue(annotationForClass.value());
                logPluginContent.add(logPluginDTO);

                return;
            }

            Method[] ms = c.getDeclaredMethods();

            if (ms.length > 0) {
                for (int i = 0; i < ms.length; i++) {
                    Method method  = ms[i];

                    if (method.getName().equals(logPluginDTO.getSqlMethor())){
                        AddLogPlugin addLogPlugin = (AddLogPlugin)method.getAnnotation(AddLogPlugin.class);

                        if (addLogPlugin != null) {

                            logPluginDTO.setValue(addLogPlugin.value());
                            logPluginContent.add(logPluginDTO);
                        }
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private LogPluginDTO invocationToLogPluginDTO(Invocation invocation) {

        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement)args[MAPPED_STATEMENT_INDEX];
        Object param = args[PARAMETER_INDEX];

        BoundSql boundSql = mappedStatement.getBoundSql(args[PARAMETER_INDEX]);
        String sql = boundSql.getSql().toLowerCase().replaceAll("`","");
        String sqlCommandType = mappedStatement.getSqlCommandType().name();

        String classPathAndSqlMethor = mappedStatement.getId();
        int lastIndexOfDoc = classPathAndSqlMethor.lastIndexOf(".");


        LogPluginDTO logPluginDTO = new LogPluginDTO();

        logPluginDTO.setCode(properties.getProperty("groupName"));
        logPluginDTO.setClassPath(classPathAndSqlMethor.substring(0,lastIndexOfDoc));
        logPluginDTO.setParam(param.toString());
        logPluginDTO.setSql(sql);
        logPluginDTO.setSqlMethor(classPathAndSqlMethor.substring(lastIndexOfDoc+1));

        logPluginDTO.setSqlType(sqlCommandType);

        return logPluginDTO;
    }

}
