package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.PermissionDao;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色权限表(Permission)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:29
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Permission findById(Long id) {
        return this.permissionDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    @Override
    public Permission insert(Permission permission) {
        this.permissionDao.insert(permission);
        return permission;
    }

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    @Override
    public Permission update(Permission permission) {
        this.permissionDao.update(permission);
        return this.findById(permission.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.permissionDao.deleteById(id) > 0;
    }
}