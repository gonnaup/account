package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.RolePermissionDao;
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
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    @Override
    public RolePermission queryById(Long roleId) {
        return this.rolePermissionDao.queryById(roleId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<RolePermission> queryAllByLimit(int offset, int limit) {
        return this.rolePermissionDao.queryAllByLimit(offset, limit);
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
     * 修改数据
     *
     * @param rolePermission 实例对象
     * @return 实例对象
     */
    @Override
    public RolePermission update(RolePermission rolePermission) {
        this.rolePermissionDao.update(rolePermission);
        return this.queryById(rolePermission.getRoleId());
    }

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long roleId) {
        return this.rolePermissionDao.deleteById(roleId) > 0;
    }
}