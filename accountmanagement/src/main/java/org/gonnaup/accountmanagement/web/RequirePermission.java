package org.gonnaup.accountmanagement.web;

/** api需要某权限访问
 * @author gonnaup
 * @version 2020/12/20 11:29
 */
public @interface RequirePermission {

    /**
     * 权限列表
     * @return
     */
    String[] value() default {};

}
