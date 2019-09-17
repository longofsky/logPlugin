package com.sky.logPlugin.annotation;

import java.lang.annotation.*;

/**
 *日志插件辅助注解，可以添加备注
 *  @author: 甜筒
 * @Date: 18:36 2018/12/13
 * Modified By:
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AddLogPlugin {

    String value() default "";
}
