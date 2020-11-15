package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.Role;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;

/**
 * 账户角色表(Role)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:30
 */
public interface RoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Role findById(Long id);

    /**
     * 查询多条数据
     * @param role 条件
     * @param pageable 分页条件
     * @return 对象页
     */
    Page<Role> findAllConditionalPaged(Role role, Pageable pageable);

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    Role insert(Role role, Operater operater);

    /**
     * 修改数据
     *
     * @param role 实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    Role update(Role role, Operater operater);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @param operater 操作者
     * @return 是否成功
     */
    boolean deleteById(Long id, Operater operater);

}