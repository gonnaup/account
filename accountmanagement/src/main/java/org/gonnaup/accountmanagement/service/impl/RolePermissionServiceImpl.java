package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.RolePermissionDao;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.gonnaup.accountmanagement.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色权限关联表(RolePermission)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * 查询某角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        return rolePermissionDao.queryPermissionsByRoleId(roleId);
    }

    /**
     * 新增数据
     *
     * @param rolePermission 实例对象
     * @return 实例对象
     */
    @Override
    public RolePermission insert(RolePermission rolePermission) {
        this.rolePermissionDao.insert(rolePermission);
        return rolePermission;
    }

    /**
     * 删除角色所有权限
     *
     * @param roleId 角色id
     * @return 是否成功
     */
    @Override
    public boolean deleteByRoleId(Long roleId) {
        return rolePermissionDao.deleteByRoleId(roleId) >= 0;
    }

    /**
     * 删除单个角色的权限
     *
     * @param roleId       角色id
     * @param permissionId 权限id
     * @return
     */
    @Override
    public boolean deleteByRoleIdAndPermissionId(Long roleId, Long permissionId) {
        return rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId) > 0;
    }
}