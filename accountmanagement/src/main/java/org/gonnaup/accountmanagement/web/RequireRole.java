package org.gonnaup.accountmanagement.web;

/**
 * api需要某角色访问
 * @author gonnaup
 * @version 2020/12/20 11:28
 */
public @interface RequireRole {
    /**
     * 角色列表
     * @return
     */
    String[] value() default {};
}
