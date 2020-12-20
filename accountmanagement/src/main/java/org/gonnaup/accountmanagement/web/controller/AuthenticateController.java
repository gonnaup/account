package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.account.enums.AuthType;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.dto.LoginDTO;
import org.gonnaup.accountmanagement.entity.Authentication;
import org.gonnaup.accountmanagement.exception.AuthenticationException;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.AuthenticationService;
import org.gonnaup.accountmanagement.util.JWTUtil;
import org.gonnaup.common.domain.Result;
import org.gonnaup.common.util.CryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 认证controller
 *
 * @author hy
 * @version 2020/12/11 11:12
 */
@Slf4j
@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    /**
     * email
     */
    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 用户密码登录方式
     *
     * @param login   登录信息
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<AccountHeader> login(@RequestBody @Validated LoginDTO login) {
        //todo 登录次数控制
        //是否是email
        String identifier = login.getIdentifier();
        if (emailPattern.matcher(identifier).matches()) {
            log.info("登录标的[{}]匹配为邮箱", identifier);
            /**
             * 匹配为邮箱
             * 先在authentication表中认证后查询账号
             */
            Optional<Authentication> authentication = authenticationService.findOne(ApplicationName.APPNAME, AuthType.E.name(), identifier);
            if (authentication.isPresent()) {
                Authentication auth = authentication.get();
                if (CryptUtil.md5Encode(new String(login.getCredential()), AuthenticateConst.SALT).equals(auth.getCredential())) {
                    //登录成功
                    //gc
                    login.setCredential(null);
                    login.setIdentifier(null);

                    Optional<AccountHeader> headerOptional = accountService.findHeaderById(auth.getAccountId());// always exists
                    AccountHeader accountHeader = headerOptional.orElseThrow();
                    log.info("登录标的[{}]登录成功，账号信息 {}", identifier, accountHeader);
                    return Result.<AccountHeader>builder().code("200")
                            .message(JWTUtil.signJWT(accountHeader.getId(), new Date(Instant.now().getEpochSecond() + AuthenticateConst.JWT_EXPIRE_TIME)))
                            .data(accountHeader).build();
                } else {
                    log.info("登录标的[{}]密码错误，登录失败", identifier);
                    throw new AuthenticationException("用户名或密码错误");
                }
            } else {
                log.info("登录标的[{}]不存在，登录失败", identifier);
                throw new AuthenticationException("用户名或密码错误");
            }
        } else {
            /**
             * 用户名
             * 先查询账号，再查询关联邮箱的密码是否匹配
             */
            Optional<AccountHeader> accountHeaderOptional = accountService.findHeaderByAccountname(ApplicationName.APPNAME, identifier);
            if (accountHeaderOptional.isPresent()) {
                AccountHeader accountHeader = accountHeaderOptional.get();
                //email认证信息
                Authentication authentication = authenticationService.findByAccountIdOfEmail(accountHeader.getId());
                if (CryptUtil.md5Encode(new String(login.getCredential()), AuthenticateConst.SALT).equals(authentication.getCredential())) {
                    log.info("登录标的[{}]登录成功，账号信息 {}", identifier, accountHeader);
                    return Result.<AccountHeader>builder().code("200")
                            .message(JWTUtil.signJWT(accountHeader.getId(), new Date(Instant.now().getEpochSecond() + AuthenticateConst.JWT_EXPIRE_TIME)))
                            .data(accountHeader).build();
                } else {
                    log.info("登录标的[{}]密码错误，登录失败", identifier);
                    throw new AuthenticationException("用户名或密码错误");
                }
            } else {
                //账号不存在
                log.info("登录标的[{}]不存在，登录失败", identifier);
                throw new AuthenticationException("用户名或密码错误");
            }
        }
    }

}
