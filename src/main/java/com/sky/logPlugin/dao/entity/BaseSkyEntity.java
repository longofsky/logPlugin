package com.sky.logPlugin.dao.entity;

import java.io.Serializable;


/**
 *
 * @brief BaseEntity 所有类的 Base 类
 * @details
 * @author 戴德荣
 * @date 2017年9月18日
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
