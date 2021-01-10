package org.gonnaup.accountmanagement.dao;

import org.gonnaup.accountmanagement.entity.ApplicationCode;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 应用代码(ApplicationCode)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:22
 */
@Repository
public interface ApplicationCodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    ApplicationCode queryById(String applicationName);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param applicationCode 实例对象
     * @return 对象列表
     */
    List<ApplicationCode> queryAllConditional(ApplicationCode applicationCode);

    /**
     * 新增数据
     *
     * @param applicationCode 实例对象
     * @return 影响行数
     */
    int insert(ApplicationCode applicationCode);

    /**
     * 修改数据
     *
     * @param applicationCode 实例对象
     * @return 影响行数
     */
    int update(ApplicationCode applicationCode);

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @return 影响行数
     */
    int deleteById(String applicationName);

}