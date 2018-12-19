package com.sky.logPlugin.test;

import com.sky.logPlugin.dao.api.IContentScanDataService;

/**
 * @author: 甜筒
 * @Date: 11:53 2018/12/17
 * Modified By:
 */
public class LogPluginThreadTest extends Thread{

    IContentScanDataService contentScanDataService;

    int i = 0;

    LogPluginThreadTest(IContentScanDataService contentScanDataService,int i) {

        this.contentScanDataService = contentScanDataService;
        this.i = i;
    }

    @Override
    public void run() {

        contentScanDataService.updateAppKeyByid(865707L,"spring-mybatis3"+i);
    }
}
