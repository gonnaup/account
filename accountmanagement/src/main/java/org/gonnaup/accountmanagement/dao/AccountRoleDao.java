package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.AccountRole;
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
     * 通过ID查询单条数据
     *
     * @param accountId 主键
     * @return 实例对象
     */
    AccountRole queryById(Long accountId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AccountRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param accountRole 实例对象
     * @return 对象列表
     */
    List<AccountRole> queryAll(AccountRole accountRole);

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
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AccountRole> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AccountRole> entities);

    /**
     * 修改数据
     *
     * @param accountRole 实例对象
     * @return 影响行数
     */
    int update(AccountRole accountRole);

    /**
     * 通过主键删除数据
     *
     * @param accountId 主键
     * @return 影响行数
     */
    int deleteById(Long accountId);

}