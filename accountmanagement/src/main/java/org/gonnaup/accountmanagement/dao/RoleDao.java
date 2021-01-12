package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.account.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户角色表(Role)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:30
 */
@Repository
public interface RoleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Role queryById(Long id);

    /**
     * 查询账户拥有的角色
     * @param accountId
     * @return 角色列表
     */
    List<Role> queryByAccountId(Long accountId);

    /**
     * 查询应用的某个角色
     * @param applicationName 应用名
     * @param roleName 角色名
     * @return
     */
    Role queryByAccountName(@Param("applicationName") String applicationName, @Param("roleName") String roleName);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param role 实例对象
     * @return 对象列表
     */
    List<Role> queryAllConditionalByLimit(@Param("role") Role role, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询符合条件的总数
     * @param role
     * @return 总条数
     */
    int countAllConditional(@Param("role") Role role);

    /**
     * 查询某应用所有角色信息
     * @param appName
     * @return
     */
    List<Role> queryByAppName(@Param("appName") String appName);

    /**
     * 新增数据
     *
     * @param role 实例对象
     * @return 影响行数
     */
    int insert(Role role);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Role> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Role> entities);

    /**
     * 修改数据，只能修改score和description
     *
     * @param role 实例对象
     * @return 影响行数
     */
    int update(Role role);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}