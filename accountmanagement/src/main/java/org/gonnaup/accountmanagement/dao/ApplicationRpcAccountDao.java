package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.ApplicationRpcAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 远程调用者账号，用于验证调用是否合法(ApplicationRpcAccount)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:23
 */
@Repository
public interface ApplicationRpcAccountDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ApplicationRpcAccount queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ApplicationRpcAccount> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param applicationRpcAccount 实例对象
     * @return 对象列表
     */
    List<ApplicationRpcAccount> queryAll(ApplicationRpcAccount applicationRpcAccount);

    /**
     * 新增数据
     *
     * @param applicationRpcAccount 实例对象
     * @return 影响行数
     */
    int insert(ApplicationRpcAccount applicationRpcAccount);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplicationRpcAccount> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ApplicationRpcAccount> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplicationRpcAccount> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<ApplicationRpcAccount> entities);

    /**
     * 修改数据
     *
     * @param applicationRpcAccount 实例对象
     * @return 影响行数
     */
    int update(ApplicationRpcAccount applicationRpcAccount);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}