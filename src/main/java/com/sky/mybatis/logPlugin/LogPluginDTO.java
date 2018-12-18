package com.sky.mybatis.logPlugin;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author: 甜筒
 * @Date: 13:58 2018/12/13
 * Modified By:
 */
public class LogPluginDTO implements Serializable,Comparable{


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
    private Integer commit;
    /** 注解业务描述*/
    private String annotationValue;
    /** 处理逻辑状态*/
    private Integer LogPluginDTOStatus;

    /** 对象创建时间*/
    private Date initTime;


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

    public Integer getCommit() {
        return commit;
    }

    public void setCommit(Integer commit) {
        this.commit = commit;
    }

    public Integer getLogPluginDTOStatus() {
        return LogPluginDTOStatus;
    }

    public void setLogPluginDTOStatus(Integer logPluginDTOStatus) {
        LogPluginDTOStatus = logPluginDTOStatus;
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    public void setAnnotationValue(String annotationValue) {
        this.annotationValue = annotationValue;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
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
                ", commit=" + commit +
                ", annotationValue='" + annotationValue + '\'' +
                ", LogPluginDTOStatus=" + LogPluginDTOStatus +
                ", initTime=" + initTime +
                '}';
    }

    public int compareTo(Object o) {

        LogPluginDTO logPluginDTO = (LogPluginDTO)o;
        return this.getInitTime().compareTo(logPluginDTO.getInitTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogPluginDTO that = (LogPluginDTO) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(classPath, that.classPath) &&
                Objects.equals(sqlMethor, that.sqlMethor) &&
                Objects.equals(sql, that.sql) &&
                Objects.equals(tableName, that.tableName) &&
                Objects.equals(sqlType, that.sqlType) &&
                Objects.equals(param, that.param) &&
                Objects.equals(stackValue, that.stackValue) &&
                Objects.equals(commit, that.commit) &&
                Objects.equals(annotationValue, that.annotationValue) &&
                Objects.equals(LogPluginDTOStatus, that.LogPluginDTOStatus) &&
                Objects.equals(initTime, that.initTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code, classPath, sqlMethor, sql, tableName, sqlType, param, stackValue, commit, annotationValue, LogPluginDTOStatus, initTime);
    }
}
