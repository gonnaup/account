package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.account.exception.LoginException;
import org.gonnaup.accountmanagement.constant.ResultCode;
import org.gonnaup.common.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理controller
 *
 * @author gonnaup
 * @version 2020/12/28 12:02
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {

    /**
     * 登录异常处理
     *
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Result<String>> loginExceptionHandler(LoginException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Result.<String>builder().code(ResultCode.LOGIN_ERROR.code()).fail().data(e.getMessage()).build());
    }

    /**
     * 权限验证异常处理
     *
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<String>> authenticationExceptionHandler(HttpServletResponse response, AuthenticationException e) {
        response.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(Result.<String>builder().code(ResultCode.AUTH_ERROR.code()).fail().data(e.getMessage()).build());
    }

    /**
     * jwt异常处理
     *
     * @return
     */
    @ExceptionHandler(JwtInvalidException.class)
    public ResponseEntity<Result<String>> jwtInvalidExceptionHandler(JwtInvalidException e) {
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(Result.<String>builder().code(ResultCode.LOGIN_ERROR.code()).fail().data(e.getMessage()).build());
    }


    @ExceptionHandler
    public ResponseEntity<Result<String>> otherHandler(Throwable e) {
        log.error("未知异常信息 {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Result.<String>builder().code(ResultCode.SYSTEM_ERROR.code()).fail().data("系统异常，请联系管理员\n邮箱：gonnaup@yeah.net").build());
    }

}
