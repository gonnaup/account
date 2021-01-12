package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.domain.Permission;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;

/**
 * 角色权限表(Permission)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:29
 */
public interface PermissionService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Permission findById(Long id);

    /**
     * 根据应用名和权限名查询权限对象
     * @param applicationName 应用名
     * @param permissionName 权限名
     * @return 权限对象
     */
    Permission findByApplicationNameAndPermissionName(String applicationName, String permissionName);

    /**
     * 分页查询
     * @param permission 条件
     * @param pageable 分页条件
     * @return 对象页
     */
    Page<Permission> findAllConditionalPaged(Permission permission, Pageable pageable);

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    Permission insert(Permission permission, Operater operater);

    /**
     * 修改数据，只能修改weight和description
     *
     * @param permission 实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    Permission update(Permission permission, Operater operater);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @param operater 操作者
     * @return 删除的对象
     */
    Permission deleteById(Long id, Operater operater);
}