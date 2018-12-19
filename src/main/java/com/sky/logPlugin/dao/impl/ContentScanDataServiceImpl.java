package com.sky.logPlugin.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.logPlugin.dao.api.ContentScanDataDAO;
import com.sky.logPlugin.dao.api.IContentScanDataService;
import com.sky.logPlugin.dao.entity.ContentScanDataEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: 甜筒
 * @Date: 20:02 2018/12/12
 * Modified By:
 */
@Service
public class ContentScanDataServiceImpl  implements IContentScanDataService {


    @Autowired
    private ContentScanDataDAO contentScanDataDAO;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional(rollbackFor = Exception.class)
    public Long insert() {

        JSONObject jsonObject = JSON.parseObject("{\"appKey\":\"40ba69a3421572fe\",\"businessType\":2,\"content\":\"https://image.mgzf.com/devSpace/2018-11/brokerRoom/7/0/537/537_1543226069601.jpg!pc.list\",\"contentId\":85025,\"contentType\":1,\"createBy\":2000031,\"createByType\":-1,\"createTime\":1544510056000,\"id\":865705,\"requestId\":\"3fd65d7cd680441c9e23be15b1dca368\",\"roomDesc\":\"{\\\"roomDesc\\\":\\\"85025\\\",\\\"roomId\\\":85025,\\\"roomNum\\\":\\\"85025\\\",\\\"roomType\\\":85025}\",\"scanResult\":1,\"scanTimes\":0,\"scene\":\"1,2,3,4,5,6,7,8,14\",\"status\":1,\"syncType\":2,\"updateTime\":1544510271000,\"valid\":1}");


        ContentScanDataEntity contentScanDataEntityS = jsonObject.toJavaObject(ContentScanDataEntity.class);
        contentScanDataEntityS.setId(null);
        contentScanDataEntityS.setAppKey("spring-logPlugin");

//        int i = 5/0;

        return contentScanDataDAO.insert(contentScanDataEntityS);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateAppKeyByid(Long id,String appKey)  {

        Long aLong =  contentScanDataDAO.updateAppKeyByid(id,appKey);

//        int i = 1/0;
        return aLong;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long insertEntity(ContentScanDataEntity contentScanDataEntity) {
        Long aLong = contentScanDataDAO.insert(contentScanDataEntity);

        publisher.publishEvent(contentScanDataEntity);

        return aLong;
    }

    public String getAppkeyById(long l) {
        return contentScanDataDAO.getAppkeyById(l);
    }
}
