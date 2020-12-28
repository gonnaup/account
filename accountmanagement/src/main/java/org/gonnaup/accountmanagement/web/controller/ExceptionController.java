package org.gonnaup.accountmanagement.web.controller;

import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.account.exception.LoginException;
import org.gonnaup.common.domain.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 异常处理controller
 * @author gonnaup
 * @version 2020/12/28 12:02
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * 登录异常处理
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public Result<String> loginExceptionHandler() {
        return null;
    }

    /**
     * 权限验证异常处理
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<String> authenticationExceptionHandler() {

        return null;
    }

    /**
     * jwt异常处理
     * @return
     */
    @ExceptionHandler(JwtInvalidException.class)
    public Result<String> jwtInvalidExceptionHandler() {
        return null;
    }

}
