package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关联表(RolePermission)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
@Repository
public interface RolePermissionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    List<RolePermission> queryByRoleId(Long roleId);

    /**
     * 新增数据
     *
     * @param rolePermission 实例对象
     * @return 影响行数
     */
    int insert(RolePermission rolePermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RolePermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RolePermission> entities);


    /**
     * 通过主键删除数据
     *
     * @param roleId
     * @return 影响行数
     */
    int deleteByRoleId(Long roleId);

    /**
     * 删除单个角色的权限
     * @param roleId
     * @param permissionId
     * @return
     */
    int deleteByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

}