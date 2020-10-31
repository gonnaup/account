package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.ManagementAccountDao;
import org.gonnaup.accountmanagement.entity.ManagementAccount;
import org.gonnaup.accountmanagement.service.ManagementAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统管理员账号(ManagementAccount)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:27
 */
@Service("managementAccountService")
public class ManagementAccountServiceImpl implements ManagementAccountService {
    @Autowired
    private ManagementAccountDao managementAccountDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ManagementAccount queryById(Long id) {
        return this.managementAccountDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ManagementAccount> queryAllByLimit(int offset, int limit) {
        return this.managementAccountDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param managementAccount 实例对象
     * @return 实例对象
     */
    @Override
    public ManagementAccount insert(ManagementAccount managementAccount) {
        this.managementAccountDao.insert(managementAccount);
        return managementAccount;
    }

    /**
     * 修改数据
     *
     * @param managementAccount 实例对象
     * @return 实例对象
     */
    @Override
    public ManagementAccount update(ManagementAccount managementAccount) {
        this.managementAccountDao.update(managementAccount);
        return this.queryById(managementAccount.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.managementAccountDao.deleteById(id) > 0;
    }
}