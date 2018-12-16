package com.sky.mybatis.plugin;

import java.sql.PreparedStatement;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Intercepts(
		{
			@Signature(type=ParameterHandler.class,method="getParameterObject",args=java.sql.Statement.class),
			@Signature(type=ParameterHandler.class,method="setParameters",args=java.sql.PreparedStatement  .class)
		})
public class MyParameterHandlerPlugin implements Interceptor{

	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("MySecondPlugin...intercept:"+invocation.getMethod());
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		// TODO Auto-generated method stub  ResultSetHandler
		System.out.println("MySecondPlugin...plugin:"+target);
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

}
