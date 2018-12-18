package com.sky.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.sky.mybatis.dao.api.ContentScanDataDAO;
import com.sky.mybatis.dao.api.IContentScanDataService;
import com.sky.mybatis.dao.entity.ContentScanDataEntity;
import com.sky.mybatis.dao.impl.mapper.ContentScanDataMapper;
import com.sky.mybatis.logPlugin.LogPluginContent;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class MyBatisTest {

	private ApplicationContext applicationContext;


	public void getSqlSessionFactory() throws IOException {
		applicationContext  = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");

	}

	/**
	 * 1、获取sqlSessionFactory对象:
	 * 		解析文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultSqlSession；
	 * 		注意：【MappedStatement】：代表一个增删改查的详细信息
	 * 
	 * 2、获取sqlSession对象
	 * 		返回一个DefaultSQlSession对象，包含Executor和Configuration;
	 * 		这一步会创建Executor对象；
	 * 
	 * 3、获取接口的代理对象（MapperProxy）
	 * 		getMapper，使用MapperProxyFactory创建一个MapperProxy的代理对象
	 * 		代理对象里面包含了，DefaultSqlSession（Executor）
	 * 4、执行增删改查方法
	 * 
	 * 总结：
	 * 	1、根据配置文件（全局，sql映射）初始化出Configuration对象
	 * 	2、创建一个DefaultSqlSession对象，
	 * 		他里面包含Configuration以及
	 * 		Executor（根据全局配置文件中的defaultExecutorType创建出对应的Executor）
	 *  3、DefaultSqlSession.getMapper（）：拿到Mapper接口对应的MapperProxy；
	 *  4、MapperProxy里面有（DefaultSqlSession）；
	 *  5、执行增删改查方法：
	 *  		1）、调用DefaultSqlSession的增删改查（Executor）；
	 *  		2）、会创建一个StatementHandler对象。
	 *  			（同时也会创建出ParameterHandler和ResultSetHandler）
	 *  		3）、调用StatementHandler预编译参数以及设置参数值;
	 *  			使用ParameterHandler来给sql设置参数
	 *  		4）、调用StatementHandler的增删改查方法；
	 *  		5）、ResultSetHandler封装结果
	 *  注意：
	 *  	四大对象每个创建的时候都有一个interceptorChain.pluginAll(parameterHandler);
	 * 
	 * @throws IOException
	 */
	@Test
	public void test01() throws IOException {

		this.getSqlSessionFactory();
		// 1、获取sqlSessionFactory对象
		SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate)applicationContext.getBean("sqlSessionTemplate");

		// 2、获取sqlSession对象
		SqlSession openSession = sqlSessionTemplate.getSqlSessionFactory().openSession();
		try {
			// 3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			ContentScanDataMapper contentScanDataMapper = openSession.getMapper(ContentScanDataMapper.class);
			ContentScanDataEntity contentScanDataEntity = contentScanDataMapper.findById(865705L);
			System.out.println(contentScanDataMapper);
			System.out.println(contentScanDataEntity);
		} finally {
			openSession.close();
		}
	}

	@Test
	public void test02() throws IOException {

		this.getSqlSessionFactory();
		ContentScanDataDAO contentScanDataDAO = (ContentScanDataDAO)applicationContext.getBean("contentScanDataDAOImpl");

		ContentScanDataEntity contentScanDataEntity1 = contentScanDataDAO.findById(865705L);

		ContentScanDataEntity contentScanDataEntity2 = contentScanDataDAO.findById(865707L);

		System.out.println(contentScanDataEntity1.toString()+contentScanDataEntity2.toString());
	}
	@Test
	public void testgetAppkeyById() throws IOException {

		this.getSqlSessionFactory();
		IContentScanDataService contentScanDataService = (IContentScanDataService)applicationContext.getBean("contentScanDataServiceImpl");

		String appkey = contentScanDataService.getAppkeyById(865705L);

		LogPluginContent logPluginContent = LogPluginContent.getLogPluginContent();

		System.out.println(logPluginContent);
		System.out.println(appkey);
	}

	@Test
	public void testUpdateAppkeyById() throws IOException {

		this.getSqlSessionFactory();
		IContentScanDataService contentScanDataService= (IContentScanDataService)applicationContext.getBean("contentScanDataServiceImpl");

		String appkey = contentScanDataService.getAppkeyById(865705L);
		try {

			contentScanDataService.updateAppKeyByid(865707L,"spring-mybatis3");
		} catch (Exception e) {
			e.printStackTrace();
		}

		LogPluginContent logPluginContent = LogPluginContent.getLogPluginContent();

        Scanner scan = new Scanner(System.in);
        String str1 = scan.next();
		System.out.println(logPluginContent);
	}

	@Test
	public void testInsert() throws IOException {

		this.getSqlSessionFactory();
		IContentScanDataService contentScanDataService= (IContentScanDataService)applicationContext.getBean("contentScanDataServiceImpl");

		Long contentScanDataEntity = contentScanDataService.insert();

		System.out.println(contentScanDataEntity);
	}

	@Test
	public void testInsertEntity() throws IOException {

		this.getSqlSessionFactory();
		IContentScanDataService contentScanDataService= (IContentScanDataService)applicationContext.getBean("contentScanDataServiceImpl");

		ContentScanDataDAO contentScanDataDAO = (ContentScanDataDAO)applicationContext.getBean("contentScanDataDAOImpl");

		ContentScanDataEntity contentScanDataEntity = contentScanDataDAO.findById(865705L);

		contentScanDataEntity.setId(null);
		contentScanDataEntity.setRequestId("insertEntity");

		Long aLong = contentScanDataService.insertEntity(contentScanDataEntity);

		System.out.println(contentScanDataEntity);
	}
	
	/**
	 * 插件原理
	 * 在四大对象创建的时候
	 * 1、每个创建出来的对象不是直接返回的，而是
	 * 		interceptorChain.pluginAll(parameterHandler);
	 * 2、获取到所有的Interceptor（拦截器）（插件需要实现的接口）；
	 * 		调用interceptor.plugin(target);返回target包装后的对象
	 * 3、插件机制，我们可以使用插件为目标对象创建一个代理对象；AOP（面向切面）
	 * 		我们的插件可以为四大对象创建出代理对象；
	 * 		代理对象就可以拦截到四大对象的每一个执行；
	 * 
		public Object pluginAll(Object target) {
		    for (Interceptor interceptor : interceptors) {
		      target = interceptor.plugin(target);
		    }
		    return target;
		  }
		
	 */
	/**
	 * 插件编写：
	 * 1、编写Interceptor的实现类
	 * 2、使用@Intercepts注解完成插件签名
	 * 3、将写好的插件注册到全局配置文件中
	 * 
	 */
	@Test
	public void testPlugin(){
		
		
	}

}
