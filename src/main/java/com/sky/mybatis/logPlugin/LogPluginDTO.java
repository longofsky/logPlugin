package com.sky.mybatis.logPlugin;

import java.io.Serializable;

/**
 * @author: 甜筒
 * @Date: 13:58 2018/12/13
 * Modified By:
 */
public class LogPluginDTO implements Serializable{


    /** 业务标识*/
    private String code;
    /** 类路径*/
    private String classPath;
    /** 执行方法*/
    private String sqlMethor;
    /** 执行sql*/
    private String sql;
    /** 目标表*/
    private String tableName;
    /** 执行sql类型 select/update/insert/delete 对应枚举类SqlTypeEnums*/
    private String sqlType;
    /** sql参数*/
    private String param;
    /** 栈帧信息的辨识*/
    private String stackValue;
    /** 事务状态*/
    private String commit;
    /** 注解业务描述*/
    private String annotationValue;

    /** 系统内执行sql 执行记录 key=code + classPath + sqlMethor + sql + param*/
    public String getKey() {

        return this.code+":"+this.classPath+":"+this.sqlMethor+":" +this.sql+":" +this.param;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getSqlMethor() {
        return sqlMethor;
    }

    public void setSqlMethor(String sqlMethor) {
        this.sqlMethor = sqlMethor;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getStackValue() {
        return stackValue;
    }

    public void setStackValue(String stackValue) {
        this.stackValue = stackValue;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    public void setAnnotationValue(String annotationValue) {
        this.annotationValue = annotationValue;
    }

    @Override
    public String toString() {
        return "LogPluginDTO{" +
                "code='" + code + '\'' +
                ", classPath='" + classPath + '\'' +
                ", sqlMethor='" + sqlMethor + '\'' +
                ", sql='" + sql + '\'' +
                ", tableName='" + tableName + '\'' +
                ", sqlType='" + sqlType + '\'' +
                ", param='" + param + '\'' +
                ", stackValue='" + stackValue + '\'' +
                ", commit='" + commit + '\'' +
                ", annotationValue='" + annotationValue + '\'' +
                '}';
    }
}
