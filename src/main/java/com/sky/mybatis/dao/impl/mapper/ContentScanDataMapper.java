package com.sky.mybatis.dao.impl.mapper;

import com.sky.mybatis.annotation.AddLogPlugin;
import com.sky.mybatis.dao.entity.ContentScanDataEntity;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author admin
 * @since 2018-12-12
 */
public interface ContentScanDataMapper extends BaseSkyMapper<ContentScanDataEntity>    {

    String getAppkeyById(@Param("id") Long id);
    String getRequestIdById(@Param("id") Long id);

    @AddLogPlugin(value = "com.sky.mybatis.dao.impl.ContentScanDataServiceImpl:updateAppKeyByid:Long:String")
    Long updateAppKeyByid(@Param("id")Long id, @Param("appKey")String appKey);

    //除了在BaseMapper中的基本的方法（CRUD），其它方法需要自己定义及实现

}

