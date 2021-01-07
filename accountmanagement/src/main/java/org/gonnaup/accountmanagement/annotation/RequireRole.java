package org.gonnaup.accountmanagement.annotation;

import org.gonnaup.accountmanagement.enums.RoleType;

import java.lang.annotation.*;

/**
 * api需要某角色访问，支持类级别和方法级别
 * 建议使用{@link  RequirePermission}
 * @author gonnaup
 * @version 2020/12/20 11:28
 * @see RequirePermission
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireRole {

    /**
     * 需要的角色列表
     *
     * @return
     */
    RoleType[] value() default {};
}
