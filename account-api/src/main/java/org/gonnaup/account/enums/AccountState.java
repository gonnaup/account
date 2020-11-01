package org.gonnaup.account.enums;

import lombok.Getter;

/**
 * 账户状态
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/1 19:23
 */
@Getter
public enum AccountState {
    // forbid 禁用状态
    F("禁用"),

    // normal 正常状态
    N("正常");

    private final String description;

    private AccountState(String description) {
        this.description = description;
    }

}
