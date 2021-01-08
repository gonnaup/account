package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.domain.Permission;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.RolePermission;

import java.util.List;
import java.util.Set;

/**
 * 角色权限关联表(RolePermission)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
public interface RolePermissionService {

    /**
     * 查询某角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> findPermissionsByRoleId(Long roleId);

    /**
     * 查询角色所有权限名
     *
     * @param roleId 角色ID
     * @return 权限名列表
     */
    Set<String> findPermissionNamesByRoleId(Long roleId);

    /**
     * 批量插入
     *
     * @param rolePermissionList
     * @param operater           操作者
     * @return
     */
    List<RolePermission> insertBatch(List<RolePermission> rolePermissionList, Operater operater);

    /**
     * 解除角色所有关联的权限
     *
     * @param roleId   角色id
     * @param appName 角色所属app
     * @param operater 操作者
     * @return 是否成功
     */
    boolean deleteByRoleId(Long roleId, String appName, Operater operater);

    /**
     * 解除某个角色的多个权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID
     * @param appName 角色所属app
     * @param operater 操作者
     * @return 删除个数
     */
    int deleteMany(Long roleId, List<Long> permissionIds, String appName, Operater operater);


}