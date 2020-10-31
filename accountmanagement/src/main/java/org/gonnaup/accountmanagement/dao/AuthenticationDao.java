package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户认证信息(Authentication)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
@Repository
public interface AuthenticationDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Authentication queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Authentication> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param authentication 实例对象
     * @return 对象列表
     */
    List<Authentication> queryAll(Authentication authentication);

    /**
     * 新增数据
     *
     * @param authentication 实例对象
     * @return 影响行数
     */
    int insert(Authentication authentication);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Authentication> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Authentication> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Authentication> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<Authentication> entities);

    /**
     * 修改数据
     *
     * @param authentication 实例对象
     * @return 影响行数
     */
    int update(Authentication authentication);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}