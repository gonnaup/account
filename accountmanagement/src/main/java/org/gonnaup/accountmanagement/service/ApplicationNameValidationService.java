package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.exception.AuthenticationException;

/** 应用名称验证服务，用于验证应用名是否和账号所属应用名一致
 * @author gonnaup
 * @version 2021/1/4 10:01
 */
public interface ApplicationNameValidationService {

    /**
     * 验证应用名是否和账号所属应用名一致
     * @param appName 应用名
     * @param accountId 账号id
     * @throws AuthenticationException 验证不通过时抛出
     */
    void checkApplicationNameOrigin(String appName, Long accountId) throws AuthenticationException;

}
