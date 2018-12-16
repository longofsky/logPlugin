package com.sky.mybatis.dao.api;

import com.sky.mybatis.dao.entity.ContentScanDataEntity;
/**
 *
 * @author admin
 * @since 2018-12-12
 */
public interface ContentScanDataDAO extends BaseSkyDAO<ContentScanDataEntity>     {

    //除了在BaseDAO中的基本的方法（CRUD），其它方法需要自己定义及实现

    public String getAppkeyById (Long id);

    public String getRequestIdById (Long id);


    Long updateAppKeyByid(Long id, String appKey);
}

