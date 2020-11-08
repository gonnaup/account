package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.RoleDao;
import org.gonnaup.accountmanagement.entity.Role;
import org.gonnaup.accountmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户角色表(Role)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:30
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Role findById(Long id) {
        return this.roleDao.queryById(id);
    }

    /**
     * 查询多条数据
     * @param role 多条件对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Role> findAllConditionalByLimit(Role role, int offset, int limit) {
        return roleDao.queryAllConditionalByLimit(role, offset, limit);
    }

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    @Override
    public Role insert(Role role) {
        this.roleDao.insert(role);
        return role;
    }

    /**
     * 修改数据
     *
     * @param role 实例对象
     * @return 实例对象
     */
    @Override
    public Role update(Role role) {
        this.roleDao.update(role);
        return this.findById(role.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.roleDao.deleteById(id) > 0;
    }
}