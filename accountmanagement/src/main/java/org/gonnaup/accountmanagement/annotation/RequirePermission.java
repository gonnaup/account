package org.gonnaup.accountmanagement.annotation;

import org.gonnaup.accountmanagement.enums.PermissionType;

import java.lang.annotation.*;

/**
 * api需要某权限访问,只支持方法级别
 *
 * @author gonnaup
 * @version 2020/12/20 11:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequirePermission {

    /**
     * 权限列表
     *
     * @return
     */
    PermissionType[] value() default {};

    /**
     * 包含权限列表，此列表为权限更高的权限，只需包含其中的一个就通过验证
     *
     * @return
     */
    PermissionType[] or() default {PermissionType.ALL, PermissionType.APP_ALL};

}
