package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.Authentication;

import java.util.List;

/**
 * 账户认证信息(Authentication)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
public interface AuthenticationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Authentication queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Authentication> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    Authentication insert(Authentication authentication);

    /**
     * 修改数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    Authentication update(Authentication authentication);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}