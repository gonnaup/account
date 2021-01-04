package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationNameValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gonnaup
 * @version 2021/1/4 10:04
 */
@Slf4j
@Service
public class ApplicationNameValidationServiceImpl implements ApplicationNameValidationService {

    @Autowired
    private AccountService accountService;

    /**
     * 验证应用名是否和账号所属应用名一致
     *
     * @param appName   应用名
     * @param accountId 账号id
     * @throws AuthenticationException 验证不通过时抛出
     */
    @Override
    public void checkApplicationNameOrigin(String appName, Long accountId) throws AuthenticationException {
        Account account = accountService.findById(accountId);
        if (account != null) {
            String applicationName = account.getApplicationName();
            if (!applicationName.equals(appName)) {
                log.error("您要修改账号[{}]的应用名[{}]与您所属的账号名[{}]不匹配，您无权限修改此账号", accountId, applicationName, appName);
                throw new AuthenticationException("您无权修改此账号");
            }
        }
    }
}
