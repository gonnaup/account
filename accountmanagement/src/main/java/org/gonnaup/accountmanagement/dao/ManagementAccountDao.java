package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.ManagementAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统管理员账号(ManagementAccount)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:26
 */
@Repository
public interface ManagementAccountDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ManagementAccount queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ManagementAccount> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param managementAccount 实例对象
     * @return 对象列表
     */
    List<ManagementAccount> queryAll(ManagementAccount managementAccount);

    /**
     * 新增数据
     *
     * @param managementAccount 实例对象
     * @return 影响行数
     */
    int insert(ManagementAccount managementAccount);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ManagementAccount> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ManagementAccount> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ManagementAccount> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<ManagementAccount> entities);

    /**
     * 修改数据
     *
     * @param managementAccount 实例对象
     * @return 影响行数
     */
    int update(ManagementAccount managementAccount);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}