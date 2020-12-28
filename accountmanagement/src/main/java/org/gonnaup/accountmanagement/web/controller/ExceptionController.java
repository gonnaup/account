package org.gonnaup.accountmanagement.web.controller;

import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.account.exception.LoginException;
import org.gonnaup.accountmanagement.constant.ResultCode;
import org.gonnaup.common.domain.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.gonnaup.common.domain.Result.builder;

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
    public Result<String> loginExceptionHandler(LoginException e) {
        return Result.<String>builder().code(ResultCode.LOGIN_ERROR.code()).fail().data(e.getMessage()).build();
    }

    /**
     * 权限验证异常处理
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<String> authenticationExceptionHandler(AuthenticationException e) {
        return Result.<String>builder().code(ResultCode.AUTH_ERROR.code()).fail().data(e.getMessage()).build();
    }

    /**
     * jwt异常处理
     * @return
     */
    @ExceptionHandler(JwtInvalidException.class)
    public Result<String> jwtInvalidExceptionHandler(JwtInvalidException e) {
        return Result.<String>builder().code(ResultCode.LOGIN_ERROR.code()).fail().data(e.getMessage()).build();
    }


    @ExceptionHandler
    public Result<String> otherHandler(Throwable e) {
        return Result.<String>builder().code(ResultCode.SYSTEM_ERROR.code()).fail().data("系统异常，请联系管理员\n邮箱：gonnaup@yeah.net").build();
    }

}
