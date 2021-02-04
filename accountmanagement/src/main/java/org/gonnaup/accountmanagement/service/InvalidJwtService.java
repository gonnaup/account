package org.gonnaup.accountmanagement.service;

/**
 * jwt失效操作接口
 *
 * @author gonnaup
 * @version 2021/2/4 10:36
 */
public interface InvalidJwtService {

    /**
     * 使账号对应已经生成的JWT失效
     *
     * @param accountId 账号ID
     */
    void invalidJWT(Long accountId);

}
