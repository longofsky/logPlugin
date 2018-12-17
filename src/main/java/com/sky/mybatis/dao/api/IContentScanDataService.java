package com.sky.mybatis.dao.api;

import com.sky.mybatis.dao.entity.ContentScanDataEntity;

/**
 * @author: 甜筒
 * @Date: 20:01 2018/12/12
 * Modified By:
 */
public interface IContentScanDataService {

    public Long insert( );

    public Long updateAppKeyByid (Long id,String appKey);

    public Long insertEntity(ContentScanDataEntity contentScanDataEntity);

    String getAppkeyById(long l);
}
