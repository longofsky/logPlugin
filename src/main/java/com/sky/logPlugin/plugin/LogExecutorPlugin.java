package com.sky.logPlugin.plugin;

import com.sky.logPlugin.annotation.AddLogPlugin;
import com.sky.logPlugin.constants.LogPluginConstant;
import com.sky.logPlugin.enums.LogPluginDTOStatusEnums;
import com.sky.logPlugin.enums.SqlTypeEnums;
import com.sky.logPlugin.enums.TransactionStatusEnum;
import com.sky.logPlugin.logPlugin.*;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class, CacheKey.class,BoundSql.class}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
        }
)
public class LogExecutorPlugin implements Interceptor,DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPluginMinitorDBImpl.class);

    @Autowired
    private LogPluginEnvironment logPluginEnvironment;

    Properties properties = null;

    private LogPluginContent logPluginContent = LogPluginContent.getLogPluginContent();

    private static final Integer MAPPED_STATEMENT_INDEX = 0;
    private static final Integer PARAMETER_INDEX = 1;
    private static final Integer ROW_BOUNDS_INDEX = 2;

    private volatile Boolean flag = true;

    /** 获取 插件持久化类型，如果未配置 默认取 "db"*/
    String durableType;



    public Object intercept(Invocation invocation) throws Throwable {

        if (flag) {
            durableType = properties.getProperty("durableType");
            if (StringUtils.isEmpty(durableType)) {
                durableType = "db";
            }
        }

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

        /** 应用执行方法处理逻辑标识*/
        Boolean methodHasLogPlugin = false;

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

            /** 先处理自身的方法*/
            Method[] m = c.getDeclaredMethods();
            methodHasLogPlugin(m,logPluginDTO,methodHasLogPlugin);

            /** 处理继承父类方法*/
            Class superClass =  c.getSuperclass();
            if (!methodHasLogPlugin && superClass != null) {
                Method[] ms = superClass.getDeclaredMethods();
                methodHasLogPlugin(ms,logPluginDTO,methodHasLogPlugin);
            }

            /** 处理实现接口方法*/
            Class<?>[] interfaceList =  c.getInterfaces();

            if (!methodHasLogPlugin && interfaceList != null && interfaceList.length > 0) {
                for (int i = 0; i < interfaceList.length; i++) {

                    Method[] mi = interfaceList[i].getDeclaredMethods();
                    methodHasLogPlugin(mi,logPluginDTO,methodHasLogPlugin);
                }
            }
            /** 开启LogPlugin监控线程*/
//            LogPluginMinitorFactory.getLogPluginMinitor(durableType,logPluginEnvironment).startExecuteLogDurableAsyn();
        } catch (Exception e) {
            LOGGER.error("LogExecutorPlugin校验mapper 上AddLogPlugin注解逻辑出错！",e);
        }
    }

    private void methodHasLogPlugin(Method[] m,LogPluginDTO logPluginDTO,Boolean methodHasLogPlugin) {

        if (m.length > 0 && !methodHasLogPlugin) {
            for (int i = 0; i < m.length; i++) {
                Method method  = m[i];

                if (method.getName().equals(logPluginDTO.getSqlMethor())){
                    AddLogPlugin addLogPlugin = (AddLogPlugin)method.getAnnotation(AddLogPlugin.class);

                    if (addLogPlugin != null) {

                        logPluginDTO.setAnnotationValue(addLogPlugin.value());
                        logPluginContent.add(logPluginDTO);

                        methodHasLogPlugin = true;
                    }
                }
            }
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

        /** 解析表名*/
        setTableName(logPluginDTO);

        return logPluginDTO;
    }

    /** 解析sql的表名*/
    private void setTableName (LogPluginDTO logPluginDTO) {

        /** 保存出现在表明前的关键词*/
        List <String> keys = new ArrayList<String>();
        /** 保存出现在表明前的下标*/
        List <String> tables = new ArrayList();

        keys.add("from");
        keys.add("update");
        keys.add("into");
        keys.add("join");

        String sql = logPluginDTO.getSql();

        if (StringUtils.isEmpty(sql)) {
            logPluginDTO.setTableName(SqlTypeEnums.UNKNOW.name());

            return;
        }

        String[] stringFragment =  sql.split("\\s+");

        for (int i = 0; i < stringFragment.length;i++) {

            if (!keys.contains(stringFragment[i])) {
                continue;
            }

            if ((i+1) >= stringFragment.length) {
                continue;
            }

            tables.add(stringFragment[i+1]);
        }
        if (tables.isEmpty()) {

            logPluginDTO.setTableName(SqlTypeEnums.UNKNOW.name());
            return;
        }
        /** 校验 子查询的可能*/

        StringBuffer stringBuffer = null;
        for (String s:tables) {

            if (s.contains("(")) {
                continue;
            }
            if (stringBuffer == null ) {
                stringBuffer = new StringBuffer(s);
            } else {
                stringBuffer.append(":").append(s);
            }
        }
        if (stringBuffer == null ) {
            logPluginDTO.setTableName(SqlTypeEnums.UNKNOW.name());
            return;
        }

        logPluginDTO.setTableName(stringBuffer.toString());

    }

    private void  waitdurable(LogPluginDTO logPluginDTO) {
        logPluginDTO.setCommit(TransactionStatusEnum.UNTRANSACTION.getIndex());
        logPluginDTO.setLogPluginDTOStatus(LogPluginDTOStatusEnums.WAITDURABLE.getIndex());
    }
    public void destroy() throws Exception {

        LogPluginMinitorFactory.getLogPluginMinitor(durableType,logPluginEnvironment).transformExecuteLogDurable();
    }
}
