package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.ManagementAccount;

import java.util.List;

/**
 * 系统管理员账号(ManagementAccount)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:27
 */
public interface ManagementAccountService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ManagementAccount queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ManagementAccount> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param managementAccount 实例对象
     * @return 实例对象
     */
    ManagementAccount insert(ManagementAccount managementAccount);

    /**
     * 修改数据
     *
     * @param managementAccount 实例对象
     * @return 实例对象
     */
    ManagementAccount update(ManagementAccount managementAccount);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}