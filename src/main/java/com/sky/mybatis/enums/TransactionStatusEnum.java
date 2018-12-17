package com.sky.mybatis.enums;

/**
 * @author: 甜筒
 * @Date: 11:33 2018/12/17
 * Modified By:
 */
public enum TransactionStatusEnum {

    BEFORECOMMIT (1,"BEFORECOMMIT"),
    COMMIT (2,"COMMIT"),
    COMPLETION (3,"COMPLETION"),
    ROLLBACK (4,"ROLLBACK");

    TransactionStatusEnum (Integer index,String desc) {

        this.index = index;
        this.desc = desc;
    }

    Integer index;

    String desc;

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
}
