package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 角色权限关联表(RolePermission)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
@Repository
public interface RolePermissionDao {

    /**
     * 查询某角色的所有权限
     * @param roleId
     * @return 权限列表
     */
    List<Permission> queryPermissionsByRoleId(Long roleId);

    /**
     * 查询角色所有权限名
     * @param roleId 角色ID
     * @return 权限名列表
     */
    Set<String> queryPermissionNamesByRoleId(Long roleId);

    /**
     * 查询某权限关联的角色
     * 用于能否删除权限判断条件
     * @param permissionId
     * @return 关联数目
     */
    int countPermissionRelated(Long permissionId);

    /**
     * 查询某角色关联的权限
     * 用于能否删除角色条件判断
     * @param roleId
     * @return 关联数目
     */
    int countRoleRelated(Long roleId);

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
     * 删除角色的所有权限
     *
     * @param roleId
     * @return 影响行数
     */
    int deleteByRoleId(Long roleId);

    /**
     * 删除单个角色的某个权限
     * @param roleId
     * @param permissionId
     * @return
     */
    int deleteByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

}