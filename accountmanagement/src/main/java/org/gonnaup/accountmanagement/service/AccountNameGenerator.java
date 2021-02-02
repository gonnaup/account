package org.gonnaup.accountmanagement.service;

/**
 * 账号名称生成器
 * @author gonnaup
 * @version 2021/2/2 19:36
 */
public interface AccountNameGenerator {

    /**
     * 根据应用名生成账号名
     * @param prefix 账号名称前缀
     * @return 账号名称
     */
    String generate(String prefix);
}
