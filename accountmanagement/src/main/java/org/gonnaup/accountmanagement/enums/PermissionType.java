package org.gonnaup.accountmanagement.enums;

/**
 * 系统权限
 * @author gonnaup
 * @version 2021/1/4 11:02
 */
public enum PermissionType {

    ALL("系统所有权限"),
    APP_ALL("应用所有权限，可以对应用数据进行所有类型操作"),
    APP_RW("应用写权限，包括新增、修改、删除"),
    APP_RA("应用新增权限"),
    APP_RU("应用修改权限"),
    APP_RD("应用删除权限"),
    APP_R("应用只读权限");

    private final String description;

    private PermissionType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
