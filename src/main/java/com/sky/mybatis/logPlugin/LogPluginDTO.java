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
    /** 执行sql类型 select/update/insert/delete*/
    private String sqlType;
    /** sql参数*/
    private String param;
    /** AddLogPlugin 注解信息*/
    private String value;
    /** 事务状态*/
    private String commit;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    @Override
    public String toString() {
        return "LogPluginDTO{" +
                "code='" + code + '\'' +
                ", classPath='" + classPath + '\'' +
                ", sqlMethor='" + sqlMethor + '\'' +
                ", sql='" + sql + '\'' +
                ", sqlType='" + sqlType + '\'' +
                ", param='" + param + '\'' +
                ", value='" + value + '\'' +
                ", commit='" + commit + '\'' +
                '}';
    }
}
