package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 应用序列(ApplicationSequence)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
@Repository
public interface ApplicationSequenceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    ApplicationSequence queryById(String applicationName);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ApplicationSequence> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param applicationSequence 实例对象
     * @return 对象列表
     */
    List<ApplicationSequence> queryAll(ApplicationSequence applicationSequence);

    /**
     * 新增数据
     *
     * @param applicationSequence 实例对象
     * @return 影响行数
     */
    int insert(ApplicationSequence applicationSequence);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplicationSequence> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ApplicationSequence> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplicationSequence> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<ApplicationSequence> entities);

    /**
     * 修改数据
     *
     * @param applicationSequence 实例对象
     * @return 影响行数
     */
    int update(ApplicationSequence applicationSequence);

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @return 影响行数
     */
    int deleteById(String applicationName);

}