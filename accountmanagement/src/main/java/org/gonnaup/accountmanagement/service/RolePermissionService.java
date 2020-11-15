package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.domain.Operater;
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
     * 批量插入
     * @param rolePermissionList
     * @param operater 操作者
     * @return
     */
    List<RolePermission> insertBatch(List<RolePermission> rolePermissionList, Operater operater);

    /**
     * 解除角色所有关联的权限
     *
     * @param roleId 角色id
     * @param operater 操作者
     * @return 是否成功
     */
    boolean deleteByRoleId(Long roleId, Operater operater);

    /**
     * 解除多个角色的权限
     * @param keys 主键列表
     * @param operater 操作者
     * @return 删除个数
     */
    int deleteMany(List<RolePermission> keys, Operater operater);


}