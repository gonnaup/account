package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统管理员账号(OperationLog)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:28
 */
@Repository
public interface OperationLogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OperationLog queryById(Long id);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param operationLog 实例对象
     * @return 对象列表
     */
    List<OperationLog> queryAllCoditionalByLimit(@Param("operationLog") OperationLog operationLog, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询符合条件的总记录数
     * @param operationLog
     * @return 总记录数
     */
    int countAllConditional(@Param("operationLog") OperationLog operationLog);

    /**
     * 新增数据
     *
     * @param operationLog 实例对象
     * @return 影响行数
     */
    int insert(OperationLog operationLog);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<OperationLog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OperationLog> entities);

    /**
     * 修改数据
     *
     * @param operationLog 实例对象
     * @return 影响行数
     */
    int update(OperationLog operationLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}