package org.gonnaup.accountmanagement.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.gonnaup.accountmanagement.enums.PermissionType.*;

/**
 * 系统角色
 *
 * @author gonnaup
 * @version 2021/1/4 11:02
 */
public enum RoleType {

    ADMIN("系统管理员", ALL),
    APPALL("应用管理员", APP_ALL),
    APPRW("应用读写角色", APP_RW),
    APPRAU("应用读、新增、修改权限", APP_R, APP_RA, APP_RU),
    APPR("应用只读角色", APP_R);


    private final String description;

    private final List<String> permissionList;

    private RoleType(String description, PermissionType... permissionTypes) {
        this.description = description;
        permissionList = Arrays.stream(permissionTypes).map(PermissionType::name).collect(Collectors.toUnmodifiableList());
    }

    public String description() {
        return description;
    }

    public List<String> permissionList() {
        return permissionList;
    }
}
