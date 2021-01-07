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

}
