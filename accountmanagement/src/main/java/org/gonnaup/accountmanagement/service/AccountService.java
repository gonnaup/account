package org.gonnaup.accountmanagement.service;


import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.AccountHeader;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;

/**
 * 账户信息(Account)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:18
 */
public interface AccountService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 账户id
     * @return 实例对象
     */
    Account findById(Long id);

    /**
     * 通过ID查询账户核心信息
     *
     * @param id 账户id
     * @return 账户核心信息
     */
    AccountHeader findHeaderById(Long id);

    /**
     * 多条件查询
     *
     * @param account  查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    Page<Account> findAllConditionalPaged(Account account, Pageable pageable);

    /**
     * 应用中的用户名是否已存在
     *
     * @param aplicationName 应用名
     * @param accountName    账户名
     * @return <code>true</code> - 已存在；<code>false</code> - 未存在
     */
    default boolean accountNameExist(String aplicationName, String accountName) {
        return findHeaderByAccountname(aplicationName, accountName) != null;
    }

    /**
     * 应用中的用户名是否已存在
     *
     * @param aplicationName  应用名
     * @param accountNickname 账户昵称
     * @return <code>true</code> - 已存在；<code>false</code> - 未存在
     */
    boolean accountNicknameExist(String aplicationName, String accountNickname);

    /**
     * 根据账号名查询账号核心信息，用于判断账号名是否已注册或登录验证
     *
     * @param applicationName 应用名
     * @param accountName     账号名
     * @return 账号核心信息 Optional
     */
    AccountHeader findHeaderByAccountname(String applicationName, String accountName);

    /**
     * 新增账户数据数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    Account insert(Account account);

    /**
     * 修改数据
     *
     * @param account 实例对象
     * @return 实例对象
     */
    Account update(Account account);

    /**
     * 禁用账号
     * @param accountId
     * @return <code>true</code>：成功；<code>false</code>：失败
     */
    boolean updateState(Long accountId, String state);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}