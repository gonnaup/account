package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.service.InvalidJwtService;
import org.springframework.stereotype.Service;

/**
 * jwt失效默认实现
 * @author gonnaup
 * @version 2021/2/4 10:41
 */
@Slf4j
@Service
public class DefaultInvalidJwtService implements InvalidJwtService {

    /**
     * 使账号对应已经生成的JWT失效
     *
     * @param accountId 账号ID
     */
    @Override
    public void invalidJWT(Long accountId) {
        // TODO JWT失效逻辑
    }
}
