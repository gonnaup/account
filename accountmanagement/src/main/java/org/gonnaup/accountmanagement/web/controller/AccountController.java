package org.gonnaup.accountmanagement.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.annotation.ApplicationNameParam;
import org.gonnaup.account.annotation.RequireLogin;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.enums.AccountState;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.constant.ResultConst;
import org.gonnaup.accountmanagement.dto.AccountQueryDTO;
import org.gonnaup.accountmanagement.enums.ResultCode;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationNameValidationService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.gonnaup.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    private ApplicationNameValidationService applicationNameValidationService;

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
    public Result<Page<Account>> list(HttpServletRequest request, AccountQueryDTO queryParam, @RequestParam("page") Integer page, @RequestParam("limit") Integer size) {
        String appName = (String) request.getAttribute(AuthenticateConst.REQUEST_ATTR_APPNAME);
        //todo ADMIN可查询所有账户列表
        queryParam.setApplicationName(appName);
        Account account = queryParam.toAccount();
        if (log.isDebugEnabled()) {
            log.debug("查询账户列表， 参数 {}， page: {}, size {}", account, page, size);
        }
        Page<Account> accountPage = accountService.findAllConditionalPaged(account, Pageable.of(page, size));
        return Result.code(ResultCode.SUCCESS.code()).success().data(accountPage);
    }

    /**
     * 手动添加一个账号
     *
     * @param app 应用名
     * @param account 账号信息
     * @return
     */
    @PostMapping("/new")
    public Result<Void> newAccount(@ApplicationNameParam String app, Account account) {
        //应用处理
        if (StringUtils.isNotBlank(account.getApplicationName())) {

        }
        return ResultConst.SUCCESS_NULL;
    }

    /**
     * 禁用账号
     *
     * @param app 应用名
     * @param accountId 账号ID
     * @return
     */
    @DeleteMapping("/disable/{accountId}")
    public Result<Void> disableAccount(@ApplicationNameParam String app, @PathVariable Long accountId) throws AuthenticationException {
        applicationNameValidationService.checkApplicationNameOrigin(app, accountId);
        accountService.updateState(accountId, AccountState.F.name());//修改账户状态
        return ResultConst.SUCCESS_NULL;
    }

}
