package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.account.domain.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限表(Permission)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:29
 */
@Repository
public interface PermissionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Permission queryById(Long id);

    /**
     * 查询应用中某个名称的权限
     * @param applicationName 应用名
     * @param permissionName 权限名
     * @return 权限对象
     */
    Permission queryByPermissionName(@Param("applicationName") String applicationName, @Param("permissionName") String permissionName);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param permission 实例对象
     * @return 对象列表
     */
    List<Permission> queryAllConditionalByLimit(@Param("permission") Permission permission, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 分页查询总记录数
     * @param permission
     * @return
     */
    int countAllConditional(@Param("permission") Permission permission);

    /**
     * 查询某应用所有权限列表
     * @param appName
     * @return
     */
    List<Permission> queryByAppName(@Param("appName") String appName);

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 影响行数
     */
    int insert(Permission permission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Permission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Permission> entities);

    /**
     * 修改数据，只能修改weight和description
     *
     * @param permission 实例对象
     * @return 影响行数
     */
    int update(Permission permission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}