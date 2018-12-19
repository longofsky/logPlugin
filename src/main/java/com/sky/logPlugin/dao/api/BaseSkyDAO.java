package com.sky.logPlugin.dao.api;

import com.sky.logPlugin.dao.entity.BaseSkyEntity;

import java.util.List;
import java.util.Map;


/**
 * @brief 默认有一些简单的增删改查（几乎都为ById类别），如有特殊的操作，请在子类中自行实现
 * @author 戴德荣
 * @date 2017.09.14
 * @param <T> BillBaseEntity的子类
 *
 */
public interface BaseSkyDAO<T extends BaseSkyEntity>  {


	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 * 使用findByIdL
	 */
	T findById(Long id);
	
	
	
	

	/**
	 * 插入
	 * @param t
	 * @return
	 * 使用insertL
	 */
	Long insert(T t);
	


	/**
	 * 记录更新
	 * @param t
	 * @return
	 * 使用updateL
	 */
	boolean update(T t);
	


	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 * 使用deleteByIdL
	 */
	 boolean deleteById(Long id);
	 


	/**
	 * 根据主键列表查询
	 * @param ids
	 * @return
	 * 使用findByIdsL
	 */
	List<T> findByIds(List<Long> ids);
	



	/**
	 * 根据主键列表查询并返回成map
	 * @param ids
	 * @return
	 * 使用findByIdsToMapL
	 */
	Map<Long,T> findByIdsToMap(List<Long> ids);




	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	Integer insertList(List<T> list) ;
	
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	Integer updateList(List<T> list) ;

	/**
	 * 根据列表删除
	 * @param ids
	 * @return
	 * @throws MogoException
	 * 使用deleteByIdsL
	 */
	Integer deleteByIds(List<Long> ids) ;
	


	/**
	 * 根据主键判断是否存在
	 * @param id
	 * @return
	 * 使用existsByIdL
	 */
	boolean existsById(Long id);
	
	




}
