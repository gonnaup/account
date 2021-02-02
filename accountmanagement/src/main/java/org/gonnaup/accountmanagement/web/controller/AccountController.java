package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.Authentication;
import org.gonnaup.account.enums.AccountState;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.domain.SimpleBooleanShell;
import org.gonnaup.accountmanagement.dto.AccountDTO;
import org.gonnaup.accountmanagement.dto.AccountQueryDTO;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountNameGenerator;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.AuthenticationService;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.gonnaup.accountmanagement.validator.ApplicationNameValidator;
import org.gonnaup.accountmanagement.vo.AccountVO;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账户controller
 *
 * @author gonnaup
 * @version 2021/1/1 15:04
 */
@Slf4j
@RestController
@RequestMapping("api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ApplicationNameValidator applicationNameValidator;

    @Autowired
    private AccountNameGenerator accountNameGenerator;

    /**
     * 判断是否有权限显示此页面，使用鉴权拦截器实现，通过验证后直接返回成功
     *
     * @return
     */
    @GetMapping("/display")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<Void> display() {
        return ResultConst.SUCCESS_NULL;
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
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Page<AccountVO> list(@JwtDataParam JwtData jwtData, @Valid AccountQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        //ADMIN可查询所有账户列表
        applicationNameValidator.putApplicationNameBaseonRole(jwtData, queryParam);
        if (log.isDebugEnabled()) {
            log.debug("查询账户列表， 参数 {}， page: {}, size {}", queryParam, page, size);
        }
        Account account = queryParam.toAccount();
        Page<Account> accountPage = accountService.findAllConditionalPaged(account, Pageable.of(page, size));
        List<AccountVO> accountVOList = accountPage.getData().stream().map(AccountVO::fromAccount).collect(Collectors.toUnmodifiableList());
        return Page.of(accountVOList, accountPage.getTotal());
    }

    /**
     * 手动添加一个账号，ADMIN用户可以为任何应用创建账号，其他有权限用户可以创建所属应用对应的账号
     *
     * @param app     应用名
     * @param account 账号信息
     * @return
     */
    @PostMapping("/add")
    @Transactional
    @RequirePermission(permissions = {PermissionType.APP_A})
    public Result<Void> newAccount(@JwtDataParam JwtData jwtData, @RequestBody @Validated AccountDTO accountDTO) {
        //应用名处理
        applicationNameValidator.judgeAndSetApplicationName(jwtData, accountDTO);

        Account account = accountDTO.toAccount();
        if (StringUtils.isBlank(account.getAccountName())) {
            account.setAccountName(generateAccountName(account.getApplicationName()));
        }
        Account inserted = accountService.insert(account);//添加账号信息
        Authentication authentication = accountDTO.createAuthentication(inserted.getId());//设置认证信息账号ID
        authenticationService.insert(authentication);//添加账号认证信息
        authentication.setCredential(null);//help gc
        log.info("账号[{}]添加账户 {} 认证信息 {}", jwtData.getAccountId(), account, authentication);
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
    @RequirePermission(permissions = {PermissionType.APP_D})
    public Result<Void> disableAccount(@JwtDataParam JwtData jwtData, @PathVariable Long accountId) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            log.error("账号id={} 不存在", accountId);
            throw new ValidationException("不存在此账号，无法操作");
        }
        applicationNameValidator.validateApplicationName(jwtData, account.getApplicationName());
        accountService.updateState(accountId, AccountState.F.name());//修改账户状态
        return ResultConst.SUCCESS_NULL;
    }

    @PutMapping("/update")
    public Result<Void> updateAccount(@JwtDataParam JwtData jwtData) {

        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 账户名是否已被占用
     *
     * @param jwtData
     * @param accountQueryDTO {@link AccountQueryDTO#accountName}
     * @return
     */
    @GetMapping("/accountNameExist")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<SimpleBooleanShell> accountNameExist(@JwtDataParam JwtData jwtData, AccountQueryDTO accountQueryDTO) {
        applicationNameValidator.judgeAndSetApplicationName(jwtData, accountQueryDTO);
        return Result.code(ResultCode.SUCCESS.code()).success().data(SimpleBooleanShell.of(accountService.accountNameExist(accountQueryDTO.getApplicationName(), accountQueryDTO.getAccountName())));
    }

    /**
     * 账户名是否已被占用
     *
     * @param jwtData
     * @param accountQueryDTO {@link AccountQueryDTO#accountNickname}
     * @return
     */
    @GetMapping("/accountNicknameExist")
    @RequirePermission(permissions = {PermissionType.APP_R})
    public Result<SimpleBooleanShell> accountNicknameExist(@JwtDataParam JwtData jwtData, @RequestParam(value = "applicationName", required = false) String appName, @RequestParam("accountNickname") String nickName) {
        ApplicationNameAccessor accessor = new TemporaryApplicationNameAccessor(appName);
        applicationNameValidator.judgeAndSetApplicationName(jwtData, accessor);
        return Result.code(ResultCode.SUCCESS.code()).success().data(SimpleBooleanShell.of(accountService.accountNameExist(accessor.getApplicationName(), nickName)));
    }

    /**
     * 生成默认的账号名称
     * @param jwtData
     * @param appName
     * @return
     */
    @GetMapping("/generateAccountname")
    @RequirePermission(permissions = PermissionType.APP_R)
    public Result<String> generateAccountName(@JwtDataParam JwtData jwtData, @RequestParam(value = "applicationName", required = false) String appName) {
        ApplicationNameAccessor accessor = new TemporaryApplicationNameAccessor(appName);
        //appName check
        applicationNameValidator.judgeAndSetApplicationName(jwtData, accessor);
        return Result.code(ResultCode.SUCCESS.code()).success().data(generateAccountName(accessor.getApplicationName()));
    }

    /**
     * 生成账号名称，自旋直到账号名不重复
     * @param appName 应用名称
     * @return 账号名称
     */
    private String generateAccountName(String appName) {
        String accountName = null;
        while (accountService.accountNameExist(appName, accountName = accountNameGenerator.generate(appName))) {
            log.info("账号名称 {} 在应用 {} 中已存在，重新生成", accountName, appName);
        }
        log.info("成功生成账号名 {}", accountName);
        return accountName;
    }

    static class TemporaryApplicationNameAccessor implements ApplicationNameAccessor {

        private String appName;

        public TemporaryApplicationNameAccessor(String appName) {
            this.appName = appName;
        }

        @Override
        public String getApplicationName() {
            return appName;
        }

        @Override
        public void setApplicationName(String applicationName) {
            this.appName = applicationName;
        }
    }

}
