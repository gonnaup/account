package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.AccountToken;

import java.util.List;

/**
 * 用户Token(AccountToken)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:21
 */
public interface AccountTokenService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AccountToken queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AccountToken> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param accountToken 实例对象
     * @return 实例对象
     */
    AccountToken insert(AccountToken accountToken);

    /**
     * 修改数据
     *
     * @param accountToken 实例对象
     * @return 实例对象
     */
    AccountToken update(AccountToken accountToken);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}