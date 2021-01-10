package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.enums.PermissionType;
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
        int score = accountRoleService.calculateAccountPermissionScore(accountId);
        return (score | PermissionType.ALL.weight()) == score;
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
        int score = accountRoleService.calculateAccountPermissionScore(accountId);
        return (score | PermissionType.APP_ALL.weight()) == score;
    }

    /**
     * 确认账号是否有某些权限
     *
     * @param accountId 账号ID
     * @param scores    权限分
     * @return <code>true or false</code>
     */
    @Override
    public boolean hasPermission(Long accountId, int... scores) {
        int accountScore = accountRoleService.calculateAccountPermissionScore(accountId);
        if (scores.length == 0) {
            return true;
        }
        int requrieScore = Arrays.stream(scores).reduce(0, (left, right) -> left | right);//合并所需的权限
        //如果所需权限位只读权限，则只需账号权限分>只读权限分，其他情况按位与
        return requrieScore == PermissionType.APP_R.weight() ? accountScore > requrieScore : (accountScore | requrieScore) == accountScore;
    }
}
