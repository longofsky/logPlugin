package com.sky.logPlugin.enums;

/**
 * @author: 甜筒
 * @Date: 11:33 2018/12/17
 * Modified By:
 */
public enum TransactionStatusEnum {

    /** SELECT或者未检测到 @transaction注解语句无需关注事务状态*/
    UNTRANSACTION (1,"UNTRANSACTION"),
    /** 事务开启但是未提交*/
    BEFORECOMMIT (2,"BEFORECOMMIT"),
    /** 事务正常提交*/
    COMMIT (3,"COMMIT"),
    /** 最终完成*/
    COMPLETION (4,"COMPLETION"),
    /** 事务回滚*/
    ROLLBACK (5,"ROLLBACK");

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
