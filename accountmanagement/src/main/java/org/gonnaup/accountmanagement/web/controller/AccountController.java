package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.enums.AccountState;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequireRole;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.dto.AccountDTO;
import org.gonnaup.accountmanagement.dto.AccountQueryDTO;
import org.gonnaup.accountmanagement.entity.Authentication;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.gonnaup.accountmanagement.service.*;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;

/**
 * 账户controller
 *
 * @author gonnaup
 * @version 2021/1/1 15:04
 */
@Slf4j
@RestController
@RequestMapping("api/account")
@RequireLogin
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ApplicationNameValidationService applicationNameValidationService;

    @Autowired
    private RolePermissionConfirmService rolePermissionConfirmService;

    @Autowired
    private ApplicationCodeService applicationCodeService;

    /**
     * 判断是否有权限显示此页面，使用鉴权拦截器实现，通过验证后直接返回成功
     *
     * @return
     */
    @GetMapping("/display")
    public Result<String> display() {
        return Result.code(ResultCode.SUCCESS.code()).success().data("");
    }

    /**
     * 分页查询
     *
     * @param request
     * @param queryParam
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public Result<Page<Account>> list(@JwtDataParam JwtData jwtData, AccountQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        //ADMIN可查询所有账户列表
        if (!rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) {
            queryParam.setApplicationName(jwtData.getAppName());
        }
        Account account = queryParam.toAccount();
        if (log.isDebugEnabled()) {
            log.debug("查询账户列表， 参数 {}， page: {}, size {}", account, page, size);
        }
        Page<Account> accountPage = accountService.findAllConditionalPaged(account, Pageable.of(page, size));
        return Result.code(ResultCode.SUCCESS.code()).success().data(accountPage);
    }

    /**
     * 手动添加一个账号，ADMIN用户可以为任何应用创建账号，其他有权限用户可以创建所属应用对应的账号
     *
     * @param app     应用名
     * @param account 账号信息
     * @return
     */
    @PostMapping("/new")
    @RequireRole(or = {RoleType.ADMIN, RoleType.APPALL, RoleType.APPRAU, RoleType.APPRAUD})
    @Transactional
    public Result<Void> newAccount(@JwtDataParam JwtData jwtData, @Validated AccountDTO accountDTO) {
        //应用名处理
        final Long accountId = jwtData.getAccountId();
        if (rolePermissionConfirmService.isAdmin(accountId)) {//admin角色
            //应用名验证
            if (StringUtils.isBlank(accountDTO.getApplicationName()) || applicationCodeService.findByPrimarykey(accountDTO.getApplicationName()) == null) {
                log.warn("系统管理员新增账号时应用名[{}]为空或不存在", accountDTO.getApplicationName());
                throw new ValidationException("应用名参数错误!");
            }
        } else {
            //非admin角色设置应用名为账号所属应用名
            accountDTO.setApplicationName(jwtData.getAppName());
        }

        Account account = accountDTO.toAccount();
        Account inserted = accountService.insert(account);//添加账号信息
        Authentication authentication = accountDTO.createAuthentication(inserted.getId());//设置认证信息账号ID
        authenticationService.insert(authentication);//添加账号认证信息
        authentication.setCredential(null);//help gc
        log.info("账号[{}]添加账户 {} 认证信息 {}", accountId, account, authentication);
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 禁用账号
     *
     * @param app       应用名
     * @param accountId 账号ID
     * @return
     */
    @DeleteMapping("/disable/{accountId}")
    @RequireRole(or = {RoleType.ADMIN, RoleType.APPALL, RoleType.APPRAUD, RoleType.APPRUD})
    public Result<Void> disableAccount(@JwtDataParam JwtData jwtData, @PathVariable Long accountId) throws AuthenticationException {
        if (!rolePermissionConfirmService.isAdmin(jwtData.getAccountId())) {//如果不是ADMIN角色则需验证appName
            applicationNameValidationService.checkApplicationNameOrigin(jwtData.getAppName(), accountId);
        }
        accountService.updateState(accountId, AccountState.F.name());//修改账户状态
        return ResultConst.SUCCESS_NULL;
    }

    @PutMapping("/update")
    public Result<Void> updateAccount(@JwtDataParam JwtData jwtData) {

        return ResultConst.SUCCESS_NULL;
    }

}
