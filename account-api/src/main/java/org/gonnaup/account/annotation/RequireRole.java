package org.gonnaup.account.annotation;

import java.lang.annotation.*;

/**
 * api需要某角色访问，支持类级别和方法级别
 * @author gonnaup
 * @version 2020/12/20 11:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireRole {
    /**
     * 角色列表
     * @return
     */
    String[] value() default {};
}
