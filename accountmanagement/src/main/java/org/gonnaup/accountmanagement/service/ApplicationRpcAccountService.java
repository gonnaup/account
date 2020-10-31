package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.ApplicationRpcAccount;

import java.util.List;

/**
 * 远程调用者账号，用于验证调用是否合法(ApplicationRpcAccount)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:23
 */
public interface ApplicationRpcAccountService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ApplicationRpcAccount queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ApplicationRpcAccount> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param applicationRpcAccount 实例对象
     * @return 实例对象
     */
    ApplicationRpcAccount insert(ApplicationRpcAccount applicationRpcAccount);

    /**
     * 修改数据
     *
     * @param applicationRpcAccount 实例对象
     * @return 实例对象
     */
    ApplicationRpcAccount update(ApplicationRpcAccount applicationRpcAccount);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}