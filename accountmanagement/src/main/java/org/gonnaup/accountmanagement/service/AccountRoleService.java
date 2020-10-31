package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.AccountRole;

import java.util.List;

/**
 * 账户权限表(AccountRole)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
public interface AccountRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param accountId 主键
     * @return 实例对象
     */
    AccountRole queryById(Long accountId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AccountRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param accountRole 实例对象
     * @return 实例对象
     */
    AccountRole insert(AccountRole accountRole);

    /**
     * 修改数据
     *
     * @param accountRole 实例对象
     * @return 实例对象
     */
    AccountRole update(AccountRole accountRole);

    /**
     * 通过主键删除数据
     *
     * @param accountId 主键
     * @return 是否成功
     */
    boolean deleteById(Long accountId);

}