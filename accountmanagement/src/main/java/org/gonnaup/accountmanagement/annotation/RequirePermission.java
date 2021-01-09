package org.gonnaup.accountmanagement.annotation;

import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.RoleType;

import java.lang.annotation.*;

/**
 * api需要某权限访问,只支持方法级别
 *
 * @author gonnaup
 * @version 2020/12/20 11:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequirePermission {

    /**
     * 权限列表
     *
     * @return
     */
    PermissionType[] permissions() default {};

    /**
     * 角色列表
     * @return
     */
    RoleType[] roles() default {};

    /**
     * 权限分数
     * @return
     */
    int[] scores() default {};

}
