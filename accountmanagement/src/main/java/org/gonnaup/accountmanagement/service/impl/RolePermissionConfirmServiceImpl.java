package org.gonnaup.accountmanagement.service.impl;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.RoleTree;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.service.AccountRoleService;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gonnaup
 * @version 2021/1/4 10:38
 */
@Slf4j
@Service
public class RolePermissionConfirmServiceImpl implements RolePermissionConfirmService {

    @Autowired
    private AccountRoleService accountRoleService;

    @Autowired
    private AccountService accountService;

    /**
     * 确认账号是否有某些权限
     *
     * @param accountId 账号ID
     * @param roles     角色
     * @return <code>true or false</code>
     */
    @Override
    public boolean hasRole(Long accountId, String... roles) {
        List<String> names = accountRoleService.findRoleNamesByAccountId(accountId);
        if (CollectionUtils.isNotEmpty(names)) {
            HashSet<String> nameSet = Sets.newHashSet(names);
            return ArrayUtils.isEmpty(roles) || nameSet.containsAll(List.of(roles));
        }
        //账号和所给角色均为空时返回true
        return ArrayUtils.isEmpty(roles);
    }

    /**
     * 确认账号是否是系统管理员账号
     *
     * @param accountId 账号ID
     * @return <code>true or false</code>
     */
    @Override
    public boolean isAdmin(Long accountId) {
        return hasRole(accountId, PermissionType.ALL.name());
    }

    /**
     * 确认账号是否是应用管理员账号
     *
     * @param accountId 账号ID
     * @return <code>true or false</code>
     */
    @Override
    public boolean isAppAdmin(Long accountId) {
        return hasRole(accountId, PermissionType.APP_ALL.name());
    }

    /**
     * 确认账号是否有某些权限
     *
     * @param accountId   账号ID
     * @param permissions 权限
     * @return <code>true or false</code>
     */
    @Override
    public boolean hasPermission(Long accountId, String... permissions) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            log.warn("账号ID[{}]不存在!", accountId);
            return false;
        }
        if (ArrayUtils.isEmpty(permissions)) {
            return true;
        }
        List<RoleTree> roleTrees = accountRoleService.findRoleTreesByAccountId(accountId, account.getApplicationName());
        Set<String> permissionOwned = roleTrees.stream().flatMap(roleTree -> roleTree.getPermissionNameSet().stream()).collect(Collectors.toSet());
        return permissionOwned.containsAll(Arrays.asList(permissions));
    }
}
