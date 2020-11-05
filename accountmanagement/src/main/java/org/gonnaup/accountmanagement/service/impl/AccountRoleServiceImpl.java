package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.AccountRoleDao;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.Role;
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
     * 查询账户的角色信息
     *
     * @param accountId 主键
     * @return 实例对象
     */
    @Override
    public List<Role> findRolesByAccountId(Long accountId) {
        return accountRoleDao.queryRolesByAccountId(accountId);
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
     * 删除账户所有角色
     *
     * @param accountId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteByAccountId(Long accountId) {
        return accountRoleDao.deleteByAccountId(accountId) >= 0;
    }

    /**
     * 删除账户单个角色
     *
     * @param accountId
     * @param roleId
     * @return
     */
    @Override
    public boolean deleteByAccountIdAndRoleId(Long accountId, Long roleId) {
        return accountRoleDao.deleteByAccountIdAndRoleId(accountId, roleId) > 0;
    }
}