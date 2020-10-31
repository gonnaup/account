package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.AccountRoleDao;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.service.AccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户权限表(AccountRole)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
@Service("accountRoleService")
public class AccountRoleServiceImpl implements AccountRoleService {
    @Autowired
    private AccountRoleDao accountRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param accountId 主键
     * @return 实例对象
     */
    @Override
    public AccountRole queryById(Long accountId) {
        return this.accountRoleDao.queryById(accountId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AccountRole> queryAllByLimit(int offset, int limit) {
        return this.accountRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param accountRole 实例对象
     * @return 实例对象
     */
    @Override
    public AccountRole insert(AccountRole accountRole) {
        this.accountRoleDao.insert(accountRole);
        return accountRole;
    }

    /**
     * 修改数据
     *
     * @param accountRole 实例对象
     * @return 实例对象
     */
    @Override
    public AccountRole update(AccountRole accountRole) {
        this.accountRoleDao.update(accountRole);
        return this.queryById(accountRole.getAccountId());
    }

    /**
     * 通过主键删除数据
     *
     * @param accountId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long accountId) {
        return this.accountRoleDao.deleteById(accountId) > 0;
    }
}