package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户权限表(AccountRole)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:19
 */
@Repository
public interface AccountRoleDao {

    /**
     * 查询账户的所有角色
     *
     * @param accountId 账户ID
     * @return 角色
     */
    List<Role> queryRolesByAccountId(Long accountId);

    /**
     * 查询账户所有角色名
     * @param accountId 账户ID
     * @return 角色名列表
     */
    List<String> queryRoleNamesByAccountId(Long accountId);

    /**
     * 查询某角色关联账户数
     * 用作能否删除角色条件判断
     * @param roleId
     * @return 关联数目
     */
    int countRoleRelated(Long roleId);

    /**
     * 新增数据
     *
     * @param accountRole 实例对象
     * @return 影响行数
     */
    int insert(AccountRole accountRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AccountRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AccountRole> entities);

    /**
     * 通过账户id删除数据
     *
     * @param accountId 账户id
     * @return 影响行数
     */
    int deleteByAccountId(Long accountId);

    /**
     * 删除账户的某个角色
     * @param accountId
     * @param roleId
     * @return
     */
    int deleteByAccountIdAndRoleId(@Param("accountId") Long accountId, @Param("roleId") Long roleId);

}