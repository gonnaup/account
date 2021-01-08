package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.gonnaup.accountmanagement.service.AccountRoleService;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 是否有某特定角色或权限的确认服务
 *
 * @author gonnaup
 * @version 2021/1/4 10:38
 */
@Slf4j
@Service
public class RolePermissionConfirmServiceImpl implements RolePermissionConfirmService {

    @Autowired
    private AccountRoleService accountRoleService;

    /**
     * 确认账号是否是系统管理员账号
     *
     * @param accountId 账号ID
     * @return <code>true or false</code>
     * @see RoleType#ADMIN
     */
    @Override
    public boolean isAdmin(Long accountId) {
        Integer score = accountRoleService.calculateAccountPermissionScore(accountId);
        return (score | RoleType.ADMIN.score()) == score;
    }

    /**
     * 确认账号是否是应用管理员账号
     *
     * @param accountId 账号ID
     * @return <code>true or false</code>
     * @see RoleType#APPALL
     */
    @Override
    public boolean isAppAdmin(Long accountId) {
        Integer score = accountRoleService.calculateAccountPermissionScore(accountId);
        return (score | RoleType.APPALL.score()) == score;
    }

    /**
     * 确认账号是否有某些权限
     *
     * @param accountId 账号ID
     * @param score     权限分
     * @return <code>true or false</code>
     */
    @Override
    public boolean hasPermission(Long accountId, Integer... scores) {
        Integer score = accountRoleService.calculateAccountPermissionScore(accountId);
        return score > 0 && scores.length == 0 || Arrays.stream(scores).allMatch(s -> (score | s) == score);
    }
}
