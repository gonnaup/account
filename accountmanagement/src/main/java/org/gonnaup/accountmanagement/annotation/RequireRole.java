package org.gonnaup.accountmanagement.annotation;

import org.gonnaup.accountmanagement.enums.RoleType;

import java.lang.annotation.*;

/**
 * api需要某角色访问，支持类级别和方法级别
 *
 * @author gonnaup
 * @version 2020/12/20 11:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireRole {

    /**
     * 至少需要的角色列表
     *
     * @return
     */
    RoleType[] value() default {};

    /**
     * 包含角色列表，此列表为权限更高的角色，只需包含其中的一个就通过验证
     *
     * @return
     */
    RoleType[] or() default {RoleType.ADMIN, RoleType.APPALL};
}
