package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.AccountToken;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Token(AccountToken)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
@Repository
public interface AccountTokenDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AccountToken queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AccountToken> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param accountToken 实例对象
     * @return 对象列表
     */
    List<AccountToken> queryAll(AccountToken accountToken);

    /**
     * 新增数据
     *
     * @param accountToken 实例对象
     * @return 影响行数
     */
    int insert(AccountToken accountToken);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AccountToken> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AccountToken> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AccountToken> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AccountToken> entities);

    /**
     * 修改数据
     *
     * @param accountToken 实例对象
     * @return 影响行数
     */
    int update(AccountToken accountToken);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}