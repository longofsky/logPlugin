package com.sky.logPlugin.plugin;

import com.sky.logPlugin.annotation.AddLogPlugin;
import com.sky.logPlugin.constants.LogPluginConstant;
import com.sky.logPlugin.enums.LogPluginDTOStatusEnums;
import com.sky.logPlugin.enums.SqlTypeEnums;
import com.sky.logPlugin.enums.TransactionStatusEnum;
import com.sky.logPlugin.logPlugin.LogPluginContent;
import com.sky.logPlugin.logPlugin.LogPluginDTO;
import com.sky.logPlugin.logPlugin.LogPluginMinitorImpl;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class, CacheKey.class,BoundSql.class}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
        }
)
public class LogExecutorPlugin implements Interceptor {

//    @Autowired (required = false)
//    private AdvisedSupport advised;
//    @Autowired (required = false)
//    private AdvisorChainFactory advisorChainFactory;
    @Autowired(required = false)
    private DataSourceTransactionManager transactionManager;

    Properties properties = null;

    private LogPluginContent logPluginContent = LogPluginContent.getLogPluginContent();

    private static final Integer MAPPED_STATEMENT_INDEX = 0;
    private static final Integer PARAMETER_INDEX = 1;
    private static final Integer ROW_BOUNDS_INDEX = 2;



    public Object intercept(Invocation invocation) throws Throwable {

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
                logPluginDTO.setAnnotationValue(annotationForClass.value());
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
                            logPluginDTO.setAnnotationValue(addLogPlugin.value());
                            logPluginContent.add(logPluginDTO);
                        }
                    }
                }
            }

            LogPluginMinitorImpl.getInstance(transactionManager.getDataSource()).startExecuteLogDurableAsyn();
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

        String threadName = Thread.currentThread().getName();
        /** select或 未开启声明式事务 语句不走事务逻辑*/
        if(SqlTypeEnums.SELECT.getDesc().equals(sqlCommandType)) {
            this.waitdurable(logPluginDTO);
        } else if (!threadName.contains(LogPluginConstant.TRANSACTIONAL_ANNOTATION_FLAG)) {
            this.waitdurable(logPluginDTO);
        }

        logPluginDTO.setStackValue(threadName);
        logPluginDTO.setInitTime(new Date());

        return logPluginDTO;
    }

    /** 提取表名信息 todo*/

    private void  waitdurable(LogPluginDTO logPluginDTO) {
        logPluginDTO.setCommit(TransactionStatusEnum.UNTRANSACTION.getIndex());
        logPluginDTO.setLogPluginDTOStatus(LogPluginDTOStatusEnums.WAITDURABLE.getIndex());
    }

}
