package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.AccountDao;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 账户信息(Account)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:18
 */
@Service("accountService")
@CacheConfig(cacheNames = {"account"})
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ApplicationSequenceService applicationSequenceService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    @Cacheable(key = "#id")
    public Account findById(Long id) {
        return accountDao.queryById(id);
    }

    /**
     * 通过ID查询账户核心信息
     *
     * @param id 账户id
     * @return 账户核心信息
     */
    @Override
    @Cacheable(key = "'accountheader::'+#id")
    public AccountHeader findHeaderById(Long id) {
        return accountDao.queryHeaderById(id);
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
        account = Optional.ofNullable(account).orElse(new Account());
        List<Account> accountList = accountDao.queryAllConditionalByLimit(account, pageable.getOffset(), pageable.getSize());
        int count = accountDao.countAllConditional(account);
        return Page.of(accountList, count);
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
        return accountDao.accountNicknameExist(aplicationName, accountNickname) > 0;
    }

    /**
     * 根据账号名查询账号核心信息，用于判断账号名是否已注册或登录验证
     *
     * @param applicationName 应用名
     * @param accountName     账号名
     * @return 账号核心信息
     */
    @Override
    public AccountHeader findHeaderByAccountname(String applicationName, String accountName) {
        return accountDao.queryHeaderByApplicationnameAndAccountname(applicationName, accountName);
    }

    /**
     * 新增数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public Account insert(Account account) {
        long id = applicationSequenceService.produceSequence(AppSequenceKey.ACCOUNT);
        account.setId(id);
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
    @Caching(put = {@CachePut(key = "#account.id", condition = "#result != null")}, evict = {@CacheEvict(key = "'accountheader::'+#account.id", condition = "#result != null")})
    @Transactional
    public Account update(Account account) {
        this.accountDao.update(account);
        return this.findById(account.getId());
    }

    /**
     * 禁用账号
     *
     * @param accountId
     * @return <code>true</code>：成功；<code>false</code>：失败
     */
    @Override
    @Caching(evict = {@CacheEvict(key = "#accountId", condition = "#result"), @CacheEvict(key = "'accountheader::'+#accountId", condition = "#result")})
    @Transactional
    public boolean updateState(Long accountId, String state) {
        return accountDao.updateAccountStateById(accountId, state) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Caching(evict = {@CacheEvict(key = "#id", condition = "#result"), @CacheEvict(key = "'accountheader::'+#id", condition = "#result")})
    @Transactional
    public boolean deleteById(Long id) {
        return this.accountDao.deleteById(id) > 0;
    }
}