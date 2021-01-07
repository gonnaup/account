package org.gonnaup.accountmanagement.enums;

import java.util.Arrays;

import static org.gonnaup.accountmanagement.enums.PermissionType.*;

/**
 * 系统角色及对应权限列表
 *
 * @author gonnaup
 * @version 2021/1/4 11:02
 */
public enum RoleType {

    ADMIN("系统管理员", ALL),
    APPALL("应用管理员", APP_ALL),
    APPRDAU("应用读取、删除、新增、修改权限", APP_D, APP_A, APP_U, APP_R),
    APPRAU("应用读取、新增、修改权限", APP_R, APP_A, APP_U),
    APPRUD("应用读取、修改、删除权限", APP_R, APP_U, APP_D),
    APPR("应用只读角色", APP_R);


    private final String description;

    private final int score;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private RoleType(String description, PermissionType... permissionTypes) {
        this.description = description;
        score = Arrays.stream(permissionTypes).mapToInt(PermissionType::weight).reduce((left, right) -> left | right).getAsInt();
    }

    public String description() {
        return description;
    }

    public int score() {
        return score;
    }
}
