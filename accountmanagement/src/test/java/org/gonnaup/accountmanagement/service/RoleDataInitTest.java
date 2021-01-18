package org.gonnaup.accountmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.constant.TestOperaters;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.gonnaup.accountmanagement.enums.PermissionType.*;

/**
 * @author gonnaup
 * @version 2021/1/7 21:28
 */
@Slf4j
@SpringBootTest
@ActiveProfiles({"default", "postgresql"})
public class RoleDataInitTest {

    @Autowired
    AccountRoleService accountRoleService;

    @Autowired
    RoleService roleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    PermissionService permissionService;


    @Test
    @Disabled
    void initData() {
        //插入角色
        Map<String, Role> roleMap = Arrays.stream(RoleType.values()).map(roleType -> {
            Role role = new Role();
            role.setApplicationName(ApplicationName.APPNAME);
            role.setRoleName(roleType.name());
            role.setScore(StringUtils.leftPad(Integer.toHexString(roleType.score()).toUpperCase(), 8, '0'));
            role.setDescription(roleType.description());
            role = roleService.insert(role, TestOperaters.ADMIN);
            log.info("插入角色 {}", role);
            return role;
        }).collect(Collectors.toMap(Role::getRoleName, role -> role));

        //插入权限
        Map<String, Permission> permissionMap = Arrays.stream(PermissionType.values()).map(permissionType -> {
            Permission permission = new Permission();
            permission.setApplicationName(ApplicationName.APPNAME);
            permission.setPermissionName(permissionType.name());
            permission.setWeight(StringUtils.leftPad(Integer.toHexString(permissionType.weight()).toUpperCase(), 8, '0'));
            permission.setDescription(permissionType.description());
            permission = permissionService.insert(permission, TestOperaters.ADMIN);
            log.info("插入权限 {}", permission);
            return permission;
        }).collect(Collectors.toMap(Permission::getPermissionName, permission -> permission));

        //账号角色
        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(102020121100000001L);
        accountRole.setRoleId(roleMap.get(RoleType.ADMIN.name()).getId());
        accountRoleService.insertBatch(List.of(accountRole), TestOperaters.ADMIN);
        log.info("插入账户角色 {}", accountRole);

        //角色权限
        Optional.ofNullable(roleMap.get(RoleType.ADMIN.name())).ifPresent(role -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionMap.get(ALL.name()).getId());
            rolePermissionService.insertBatch(List.of(rolePermission), TestOperaters.ADMIN);
            log.info("插入角色权限 {}", rolePermission);
        });

        Optional.ofNullable(roleMap.get(RoleType.APPALL.name())).ifPresent(role -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionMap.get(APP_ALL.name()).getId());
            rolePermissionService.insertBatch(List.of(rolePermission), TestOperaters.ADMIN);
            log.info("插入角色权限 {}", rolePermission);
        });

        List.of(APP_D, APP_A, APP_U, APP_R).parallelStream().map(Enum::name).forEach(s -> Optional.ofNullable(permissionMap.get(s)).ifPresent(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleMap.get(RoleType.APPRDAU.name()).getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermissionService.insertBatch(List.of(rolePermission), TestOperaters.ADMIN);
            log.info("插入角色权限 {}", rolePermission);
        }));

        List.of(APP_R, APP_A, APP_U).parallelStream().map(Enum::name).forEach(s -> Optional.ofNullable(permissionMap.get(s)).ifPresent(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleMap.get(RoleType.APPRAU.name()).getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermissionService.insertBatch(List.of(rolePermission), TestOperaters.ADMIN);
            log.info("插入角色权限 {}", rolePermission);
        }));

        List.of(APP_R, APP_U, APP_D).parallelStream().map(Enum::name).forEach(s -> Optional.ofNullable(permissionMap.get(s)).ifPresent(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleMap.get(RoleType.APPRUD.name()).getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermissionService.insertBatch(List.of(rolePermission), TestOperaters.ADMIN);
            log.info("插入角色权限 {}", rolePermission);
        }));

        List.of(APP_R).parallelStream().map(Enum::name).forEach(s -> Optional.ofNullable(permissionMap.get(s)).ifPresent(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleMap.get(RoleType.APPR.name()).getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermissionService.insertBatch(List.of(rolePermission), TestOperaters.ADMIN);
            log.info("插入角色权限 {}", rolePermission);
        }));

    }

}
