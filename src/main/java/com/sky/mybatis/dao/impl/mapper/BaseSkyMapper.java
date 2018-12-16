package com.sky.mybatis.dao.impl.mapper;

import com.sky.mybatis.dao.entity.BaseSkyEntity;

import java.util.List;


/**
 * 替换 BaseMapper 主要变化是ID 变成long型
 * @brief BaseMapper 基础mapper
 * @details 具体说明
 * @author 戴德荣
 * @date 2017年9月17日
 *
 */
public interface BaseSkyMapper<T extends BaseSkyEntity> {

	/**
	 * 请不要使用po这种方式传递，参数请使用对象直接传
	 * @deprecated
	 */
	@Deprecated
	public static final String PO_KEY = "po";

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 * 使用findByIdL
	 */
	T findById(Long id);
	


	/**
	 * 对象插入
	 * @param entity
	 * @return
	 */
	Long insert(T entity);
	


	/**
	 * 对象更新
	 * @param entity
	 * @return
	 */
	Long update(T entity);
	
	 
	/**
	 * 将表中的数据逻辑删除
	 * @details 将valid设置为0，并不是物理删除数据
	 * @param id ID,不能为空
	 * @return Long 返回更新条数，这里仅为1表示成功
	 * @author 戴德荣
	 * @date 2017年9月16日
	 * 使用deleteByIdL
	 */
	Long deleteById(Long id);
	

	
	/**
	 *  批量查找数据
	 * @details
	 * @param ids List<Long> ID列表，注意不能超过架构组定义的值,且size至少>=1
	 * @return List<T> 对象列表
	 * @author 戴德荣
	 * @date 2017年9月16日
	 * 使用findByIdsL
	 */
	List<T> findByIds(List<Long> ids);

	
	
	/**
	 *  批量插入数据
	 * @details
	 * @param list List<T> entity列表,注意list的大小，不能超过架构组定义的值，且size至少>=1
	 * @return Long  插入的条数
	 * @author 戴德荣
	 * @date 2017年9月16日
	 */
	Integer insertList(List<T> list);

	/**
	 *  批量更新数据
	 * @details
	 * @param list List<T> entity列表,注意list的大小，不能超过架构组定义的值，且size至少>=1
	 * @return Long 更新的条数
	 * @author 戴德荣
	 * @date 2017年9月16日
	 */
	Integer updateList(List<T> list);

	/**
	 *  通过ID批量删除数据
	 * @details
	 * @param ids  List<Long> ID列表，注意list的大小，不能超过架构组定义的值，且size至少>=1
	 * @return Long 删除的条数
	 * @author 戴德荣
	 * @date 2017年9月16日
	 * 使用deleteByIdsL
	 */
	Integer deleteByIds(List<Long> ids);
	
	
	/**
	 *  通过ID判读是否存在
	 * @details
	 * 由于底层使用的是select 1 from table where id = ? 因此 可能会导致为null，因此判断时需要特别注意，只需要判断是否为null即可
	 * 请注意在上层封装，防止为null的异常
	 * eg:	
	 * 		Long exists = existsById(1);
	 * 		return exists != null;
	 * 
	 * @param  id
	 * @return Long 不为空为存在，null为不存在，可能会在后续优化
	 * @author 戴德荣
	 * @date 2017年9月16日
	 * 使用existsByIdL
	 */
	Integer existsById(Long id);
	



}
