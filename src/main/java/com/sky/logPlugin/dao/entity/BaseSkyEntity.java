package com.sky.logPlugin.dao.entity;

import java.io.Serializable;


/**
 * @Description BaseEntity 所有类的 Base 类
 * @Author litianlong
 * @Date 2019-09-17 15:06
 */
public abstract class BaseSkyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long id;

    /**
     *
     * @return
     * 使用getIdL
     */
    public Long getId() {
        return id;
    }
    /**
     *
     * @param id
     * 使用setIdL
     */
    public void setId(Long id) {
        this.id = id;
    }


}
