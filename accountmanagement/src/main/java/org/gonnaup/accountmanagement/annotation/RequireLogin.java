package org.gonnaup.accountmanagement.annotation;

import java.lang.annotation.*;

/**
 * api需要登录访问，支持类级别和方法级别
 *
 * @author gonnaup
 * @version 2020/12/20 11:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireLogin {
}
