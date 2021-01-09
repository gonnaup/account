package org.gonnaup.accountmanagement.service;

/**
 * 是否有某特定角色或权限的确认服务
 *
 * @author gonnaup
 * @version 2021/1/4 10:32
 */
public interface RolePermissionConfirmService {

    /**
     * 确认账号是否是系统管理员账号
     *
     * @param accountId 账号ID
     * @return <code>true or false</code>
     * @see org.gonnaup.accountmanagement.enums.RoleType#ADMIN
     */
    boolean isAdmin(Long accountId);

    /**
     * 确认账号是否是应用管理员账号
     * @param accountId 账号ID
     * @return <code>true or false</code>
     * @see org.gonnaup.accountmanagement.enums.RoleType#APPALL
     */
    boolean isAppAdmin(Long accountId);

    /**
     * 确认账号是否有某些权限
     * @param accountId 账号ID
     * @param score 权限分
     * @return <code>true or false</code>
     */
    boolean hasPermission(Long accountId, int... score);


}
