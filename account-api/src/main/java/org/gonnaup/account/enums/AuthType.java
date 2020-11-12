package org.gonnaup.account.enums;

/**
 * 账户认证类型
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/6 10:50
 */
public enum AuthType {

    P("密码"),
    E("邮箱"),
    W("微信");

    private final String description;

    private AuthType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
