package com.sky.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author: 甜筒
 * @Date: 18:36 2018/12/13
 * Modified By:
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AddLogPlugin {

    String value();
}
