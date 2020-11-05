package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.entity.RolePermission;

import java.util.List;

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
     * 新增数据
     *
     * @param rolePermission 实例对象
     * @return 实例对象
     */
    RolePermission insert(RolePermission rolePermission);

    /**
     * 删除角色所有权限
     *
     * @param roleId 角色id
     * @return 是否成功
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * 删除单个角色的权限
     * @param roleId 角色id
     * @param permissionId 权限id
     * @return
     */
    boolean deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);


}