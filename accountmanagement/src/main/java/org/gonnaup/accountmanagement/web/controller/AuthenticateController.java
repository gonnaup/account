package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.enums.AuthType;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.account.exception.LoginException;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.dto.LoginDTO;
import org.gonnaup.accountmanagement.entity.Authentication;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.AuthenticationService;
import org.gonnaup.accountmanagement.util.JWTUtil;
import org.gonnaup.accountmanagement.util.RequestUtil;
import org.gonnaup.common.domain.Result;
import org.gonnaup.common.util.CryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 认证controller
 *
 * @author gonnaup
 * @version 2020/12/11 11:12
 */
@Slf4j
@RestController
@RequestMapping("/api/authenticate")
public class AuthenticateController {

    /**
     * email
     */
    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用户密码登录方式
     *
     * @param login   登录信息
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<AccountHeader> login(@RequestBody @Validated LoginDTO login, HttpServletRequest request) throws LoginException {
        String ipAddr = RequestUtil.obtainRealIpAddr(request);
        //todo 登录次数控制
        //是否是email
        String identifier = login.getIdentifier();
        if (emailPattern.matcher(identifier).matches()) {
            log.info("登录标的[{}]匹配为邮箱", identifier);
            /**
             * 匹配为邮箱
             * 先在authentication表中认证后查询账号
             */
            Optional<Authentication> authentication = Optional.ofNullable(authenticationService.findOne(ApplicationName.APPNAME, AuthType.E.name(), identifier));
            if (authentication.isPresent()) {
                Authentication auth = authentication.get();
                if (CryptUtil.md5Encode(new String(login.getCredential()), AuthenticateConst.SALT).equals(auth.getCredential())) {
                    //登录成功
                    //gc
                    login.setCredential(null);
                    login.setIdentifier(null);

                    Optional<AccountHeader> headerOptional = Optional.ofNullable(accountService.findHeaderById(auth.getAccountId()));// always exists
                    AccountHeader accountHeader = headerOptional.orElseThrow();
                    log.info("登录标的[{}]登录成功，账号信息 {}", identifier, accountHeader);
                    return Result.code(ResultCode.SUCCESS.code())
                            .message(JWTUtil.signJWT(accountHeader.getId(), new Date(Instant.now().toEpochMilli() + AuthenticateConst.JWT_EXPIRE_TIME)))
                            .data(accountHeader);
                } else {
                    log.info("登录标的[{}]密码错误，登录失败", identifier);
                    throw new LoginException("用户名或密码错误");
                }
            } else {
                log.info("登录标的[{}]不存在，登录失败", identifier);
                throw new LoginException("用户名或密码错误");
            }
        } else {
            /**
             * 用户名
             * 先查询账号，再查询关联邮箱的密码是否匹配
             */
            Optional<AccountHeader> accountHeaderOptional = Optional.ofNullable(accountService.findHeaderByAccountname(ApplicationName.APPNAME, identifier));
            if (accountHeaderOptional.isPresent()) {
                AccountHeader accountHeader = accountHeaderOptional.get();
                //email认证信息
                Authentication authentication = authenticationService.findByAccountIdOfEmail(accountHeader.getId());
                if (CryptUtil.md5Encode(new String(login.getCredential()), AuthenticateConst.SALT).equals(authentication.getCredential())) {
                    log.info("登录标的[{}]登录成功，账号信息 {}", identifier, accountHeader);
                    return Result.code(ResultCode.SUCCESS.code())
                            .message(JWTUtil.signJWT(accountHeader.getId(), new Date(Instant.now().toEpochMilli() + AuthenticateConst.JWT_EXPIRE_TIME)))
                            .data(accountHeader);
                } else {
                    log.info("登录标的[{}]密码错误，登录失败", identifier);
                    throw new LoginException("用户名或密码错误");
                }
            } else {
                //账号不存在
                log.info("登录标的[{}]不存在，登录失败", identifier);
                throw new LoginException("用户名或密码错误");
            }
        }
    }

    /**
     * jwt登录
     *
     * @param request
     * @return
     */
    @GetMapping("/authenticationJWT")
    public Result<AccountHeader> authenticationJWT(HttpServletRequest request) {
        JwtData jwtData = null;
        String jwt = null;
        try {
            jwt = RequestUtil.obtainJWT(request);
            if (redisTemplate.opsForValue().get(AuthenticateConst.JWT_BLACKLIST_REDIS_PREFIX + jwt) != null) {
                log.info("jwt {} 已被注销", jwt);
                return Result.code(ResultCode.LOGIN_ERROR.code()).message("登录凭证已过期").data(null);
            }
            jwtData = JWTUtil.jwtVerified(jwt);
        } catch (JwtInvalidException e) {
            log.warn("jwt 错误， {}", e.getMessage());
            return Result.code(ResultCode.LOGIN_ERROR.code()).message(e.getMessage()).data(null);
        }
        AccountHeader accountHeader = accountService.findHeaderById(jwtData.getAccountId());
        if (log.isDebugEnabled()) {
            log.debug("jwt {} 验证通过， 账号信息 {}", jwt, accountHeader);
        }
        return Result.code(ResultCode.SUCCESS.code()).success().data(accountHeader);
    }

    /**
     * 注销账户
     *
     * @param request
     * @return
     */
    @DeleteMapping("/signout")
    public Result<String> signout(HttpServletRequest request) {
        String jwt = RequestUtil.obtainJWT(request);
        try {
            JwtData jwtData = JWTUtil.jwtVerified(jwt);
            //设置过期时间为jwt过期剩余时间
            redisTemplate.opsForValue().set(AuthenticateConst.JWT_BLACKLIST_REDIS_PREFIX + jwt, "", Duration.ofMillis(jwtData.getRemainder()));
            log.info("jwt {} 注销成功", jwt);
        } catch (JwtInvalidException e) {
            log.info("jwt {} 已失效，无需注销", jwt);
        }
        return Result.code(ResultCode.SUCCESS.code()).success().data("");
    }



}
