package com.sky.logPlugin.dao.impl;


import com.sky.logPlugin.dao.api.BaseSkyDAO;
import com.sky.logPlugin.dao.entity.BaseSkyEntity;
import com.sky.logPlugin.dao.impl.mapper.BaseSkyMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 戴德荣
 * @brief 针对数据库的Mapper的一些封装，主要是CRUD及批量的方法
 * @details
 * @date 2017年9月18日
 */
public abstract class BaseSkyDAOImpl<M extends BaseSkyMapper<T>, T extends BaseSkyEntity>
        implements BaseSkyDAO<T> {

    /**
     * 需要注入的entityMapper，BaseMapper<T>
     */
    protected M entityMapper;

    /**
     * 设置实际Mapper
     *
     * @param mapper
     */
    public abstract void setEntityMapper(M mapper);

    /**
     * @param id 不能为空
     * @return
     */
    @Override
    public T findById(Long id) {
        return entityMapper.findById(id);
    }


    /**
     * @param t entity对象
     * @return 使用insertL
     */
    @Override
    public Long insert(T t) {
        entityMapper.insert(t);
        return t.getId();
    }


    /**
     * @param t entity对象 不能为空，且 ID必须不为null
     * @return
     */
    @Override
    public boolean update(T t) {
        return entityMapper.update(t) == 1;
    }

    @Override
    public boolean deleteById(Long id) {
        return entityMapper.deleteById(id) == 1;
    }


    /**
     * @param ids List<Long> ID列表，注意不能超过架构组定义的值,且size至少>=1
     * @return 使用findByIdsL
     */
    @Override
    public List<T> findByIds(List<Long> ids) {
        return entityMapper.findByIds(ids);
    }


    /**
     * @param ids List<Long> ID列表，注意不能超过架构组定义的值,且size至少>=1
     * @return 使用findByIdsToMapL
     */
    @Override
    public Map<Long, T> findByIdsToMap(List<Long> ids) {

        List<T> list = findByIds(ids);
        LinkedHashMap<Long, T> linkedMap = new LinkedHashMap();
        if (!list.isEmpty()) {
            for (T t : list) {
                linkedMap.put(t.getId(), t);
            }
        }
        return linkedMap;

    }


    /**
     * @param list List<T> entity对象,注意list的大小，不能超过架构组定义的值
     * @return
     */
    @Override
    public Integer insertList(List<T> list) {
        return entityMapper.insertList(list);
    }

    /**
     * @param list List<T> 注意list的大小，不能超过架构组定义的值,且size至少>=1
     * @return
     */
    @Override
    public Integer updateList(List<T> list) {
        return entityMapper.updateList(list);
    }

    /**
     * @param ids List<Long> 注意list的大小，不能超过架构组定义的值，且size至少>=1
     * @return 使用deleteByIdsL
     */
    @Override
    public Integer deleteByIds(List<Long> ids) {
        return entityMapper.deleteByIds(ids);
    }


    /**
     * @param id
     * @return 使用existsByIdL
     */
    @Override
    public boolean existsById(Long id) {
        Integer exists = entityMapper.existsById(id);
        return (exists != null && exists != 0);
    }


}
