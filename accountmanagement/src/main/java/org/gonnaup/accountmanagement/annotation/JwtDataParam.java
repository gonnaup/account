package org.gonnaup.accountmanagement.annotation;

import java.lang.annotation.*;

/** 标注{@link org.gonnaup.accountmanagement.domain.JwtData} 参数并自动复制
 * @author gonnaup
 * @version 2021/1/5 10:49
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface JwtDataParam {
}
