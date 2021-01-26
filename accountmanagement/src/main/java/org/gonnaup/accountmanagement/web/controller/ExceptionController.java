package org.gonnaup.accountmanagement.web.controller;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.*;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.common.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Result<String> authenticationExceptionHandler(HttpServletResponse response, AuthenticationException e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return Result.code(ResultCode.AUTH_ERROR.code()).fail().data(e.getMessage());
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

    /**
     * 逻辑验证异常处理
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(LogicValidationException.class)
    public Result<String> logicValidationExceptionHandler(HttpServletResponse response, LogicValidationException e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return Result.code(ResultCode.LOGIN_ERROR.code()).fail().data(e.getMessage());
    }

    /**
     * 数据验证异常处理
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    public Result<String> validationExceptionHandler(HttpServletResponse response, ValidationException e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return Result.code(ResultCode.DATA_VALIDATE_ERROR.code()).fail().data(e.getMessage());
    }

    /**
     * 数据验证异常处理
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<String> bindExceptionHandler(HttpServletResponse response, BindingResult e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return Result.code(ResultCode.DATA_VALIDATE_ERROR.code()).fail().data(Joiner.on(',').join(e.getAllErrors().stream().map(ObjectError::getDefaultMessage).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList())));
    }

    @ExceptionHandler(RelatedDataExistsException.class)
    public Result<String> relatedDataExistesExceptionHandler(HttpServletResponse response, RelatedDataExistsException e) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return Result.code(ResultCode.LOGIC_VALIDATE_ERROR.code()).fail().data(e.getMessage());
    }

    @ExceptionHandler
    public Result<String> otherHandler(HttpServletResponse response, Throwable e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("未知异常信息 {}", e.getMessage(), e);
        return Result.code(ResultCode.SYSTEM_ERROR.code()).fail().data("系统异常，请联系管理员\n邮箱：gonnaup@yeah.net");
    }

}
