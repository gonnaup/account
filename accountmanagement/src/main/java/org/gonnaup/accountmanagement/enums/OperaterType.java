package org.gonnaup.accountmanagement.enums;

/**
 * 操作员类型
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/2 15:28
 */
public enum OperaterType {

    A("系统管理员"),
    S("应用管理员");

    private final String description;

    private OperaterType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
