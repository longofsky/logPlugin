package com.sky.logPlugin.enums;

/**
 * @author: 甜筒
 * @Date: 10:59 2018/12/18
 * Modified By:
 */
public enum LogPluginDTOStatusEnums {

    /** 进入 需确认事务状态集合*/
    REQUIRETRANSACTIONVERIFY(1,"REQUIRETRANSACTIONVERIFY"),
    /** 带持久化集合*/
    WAITDURABLE(2,"WAITDURABLE");


    private Integer index;
    private String desc;

    LogPluginDTOStatusEnums (Integer index,String desc) {

        this.index = index;
        this.desc = desc;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "LogPluginDTOStatusEnums{" +
                "index=" + index +
                ", desc='" + desc + '\'' +
                '}';
    }
}
