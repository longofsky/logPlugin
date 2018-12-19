package com.sky.logPlugin.enums;

/**
 * @author: 甜筒
 * @Date: 17:04 2018/12/17
 * Modified By:
 */
public enum SqlTypeEnums {

    SELECT (1,"SELECT"),
    UPDATE (2,"UPDATE"),
    INSERT (3,"INSERT"),
    DELETE (4,"DELETE");


    private Integer index;
    private String desc;

    SqlTypeEnums (Integer index,String desc) {

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
        return "SqlTypeEnums{}";
    }
}
