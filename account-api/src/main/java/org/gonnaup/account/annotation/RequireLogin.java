package org.gonnaup.account.annotation;

import java.lang.annotation.*;

/**
 * api需要登录访问
 *
 * @author gonnaup
 * @version 2020/12/20 11:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequireLogin {

    /**
     * 是否需要登录，默认需要登录
     *
     * @return
     */
    boolean value() default true;

}
