package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.accountmanagement.dao.AccountDao;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账户信息(Account)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:18
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Account findById(Long id) {
        return this.accountDao.queryById(id);
    }

    /**
     * 通过ID查询账户核心信息
     *
     * @param id 账户id
     * @return 账户核心信息
     */
    @Override
    public AccountHeader findHeaderById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 多条件查询
     *
     * @param account  查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    @Override
    public Page<Account> findAllConditionalPaged(Account account, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 应用中的用户名是否已存在
     *
     * @param aplicationName 应用名
     * @param accountName    账户名
     * @return <code>true</code> - 已存在；<code>false</code> - 未存在
     */
    @Override
    public boolean accountNameExist(String aplicationName, String accountName) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 应用中的用户名是否已存在
     *
     * @param aplicationName  应用名
     * @param accountNickname 账户昵称
     * @return <code>true</code> - 已存在；<code>false</code> - 未存在
     */
    @Override
    public boolean accountNicknameExist(String aplicationName, String accountNickname) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 新增数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    @Override
    public Account insert(Account account) {
        this.accountDao.insert(account);
        return account;
    }

    /**
     * 修改数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    @Override
    public Account update(Account account) {
        this.accountDao.update(account);
        return this.findById(account.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.accountDao.deleteById(id) > 0;
    }
}