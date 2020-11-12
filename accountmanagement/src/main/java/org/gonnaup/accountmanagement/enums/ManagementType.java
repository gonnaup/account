package org.gonnaup.accountmanagement.enums;

/**
 * 管理员类型枚举
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/2 15:08
 */
public enum ManagementType {

    A("系统管理员"),
    S("应用管理员");

    private final String description;

    private ManagementType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
