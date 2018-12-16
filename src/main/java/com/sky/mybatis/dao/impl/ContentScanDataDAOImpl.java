package com.sky.mybatis.dao.impl;

import com.sky.mybatis.dao.api.ContentScanDataDAO;
import com.sky.mybatis.dao.entity.ContentScanDataEntity;
import com.sky.mybatis.dao.impl.mapper.ContentScanDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *
 * @author admin
 * @since 2018-12-12
 */
@Repository
public class ContentScanDataDAOImpl extends BaseSkyDAOImpl<ContentScanDataMapper, ContentScanDataEntity> implements ContentScanDataDAO {
    @Resource(type = ContentScanDataMapper.class )
    @Override
    public void setEntityMapper(ContentScanDataMapper mapper) {
        entityMapper = mapper;
    }

    public String getAppkeyById(Long id) {
        return entityMapper.getAppkeyById(id);
    }

    public String getRequestIdById(Long id) {
        return entityMapper.getRequestIdById(id);
    }

    public Long updateAppKeyByid(Long id, String appKey) {
        return entityMapper.updateAppKeyByid(id,appKey);
    }
}
