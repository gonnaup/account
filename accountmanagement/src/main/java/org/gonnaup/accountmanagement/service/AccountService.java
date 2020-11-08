package org.gonnaup.accountmanagement.service;


import org.gonnaup.account.domain.Account;

import java.util.List;

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
     * @param id 主键
     * @return 实例对象
     */
    Account findById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Account> findAllByLimit(int offset, int limit);

    /**
     * 新增数据
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
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}