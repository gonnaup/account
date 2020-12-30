package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.account.exception.LoginException;
import org.gonnaup.accountmanagement.constant.ResultCode;
import org.gonnaup.common.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理controller
 *
 * @author gonnaup
 * @version 2020/12/28 12:02
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    /**
     * 登录异常处理
     *
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public Result<String> loginExceptionHandler(HttpServletResponse response, LoginException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return Result.code(ResultCode.LOGIN_ERROR.code()).fail().data(e.getMessage());
    }

    /**
     * 权限验证异常处理
     *
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<String>> authenticationExceptionHandler(HttpServletResponse response, AuthenticationException e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(Result.code(ResultCode.AUTH_ERROR.code()).fail().data(e.getMessage()));
    }

    /**
     * jwt异常处理
     *
     * @return
     */
    @ExceptionHandler(JwtInvalidException.class)
    public Result<String> jwtInvalidExceptionHandler(HttpServletResponse response, JwtInvalidException e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return Result.code(ResultCode.NOTLOGIN_ERROR.code()).fail().data(e.getMessage());
    }


    @ExceptionHandler
    public Result<String> otherHandler(HttpServletResponse response, Throwable e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("未知异常信息 {}", e.getMessage(), e);
        return Result.code(ResultCode.SYSTEM_ERROR.code()).fail().data("系统异常，请联系管理员\n邮箱：gonnaup@yeah.net");
    }

}
