package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.AccountTokenDao;
import org.gonnaup.accountmanagement.entity.AccountToken;
import org.gonnaup.accountmanagement.service.AccountTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户Token(AccountToken)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:21
 */
@Service("accountTokenService")
public class AccountTokenServiceImpl implements AccountTokenService {
    @Autowired
    private AccountTokenDao accountTokenDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AccountToken queryById(Long id) {
        return this.accountTokenDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AccountToken> queryAllByLimit(int offset, int limit) {
        return this.accountTokenDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param accountToken 实例对象
     * @return 实例对象
     */
    @Override
    public AccountToken insert(AccountToken accountToken) {
        this.accountTokenDao.insert(accountToken);
        return accountToken;
    }

    /**
     * 修改数据
     *
     * @param accountToken 实例对象
     * @return 实例对象
     */
    @Override
    public AccountToken update(AccountToken accountToken) {
        this.accountTokenDao.update(accountToken);
        return this.queryById(accountToken.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.accountTokenDao.deleteById(id) > 0;
    }
}