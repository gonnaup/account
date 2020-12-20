package org.gonnaup.accountmanagement.web;

/** api需要登录访问
 * @author gonnaup
 * @version 2020/12/20 11:29
 */
public @interface RequireLogin {

    /**
     * 是否需要登录，默认需要登录
     * @return
     */
    boolean value() default true;

}
