package org.gonnaup.accountmanagement.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.gonnaup.accountmanagement.service.AccountNameGenerator;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 默认账号名称生成器
 * @author gonnaup
 * @version 2021/2/2 19:41
 */
@Service
public class DefaultAccountNameGenerator implements AccountNameGenerator {

    /**
     * 根据应用名生成账号名
     *
     * @param prefix 账号名称前缀
     * @return 账号名称
     */
    @Override
    public String generate(String prefix) {
        Objects.requireNonNull(prefix, "应用名称前缀不能为空");
        return prefix + '_' + RandomStringUtils.randomAlphabetic(5);
    }
}
