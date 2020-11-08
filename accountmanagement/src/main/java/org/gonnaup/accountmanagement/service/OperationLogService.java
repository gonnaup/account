package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.entity.OperationLog;

import java.util.List;

/**
 * 系统管理员账号(OperationLog)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:28
 */
public interface OperationLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OperationLog findById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<OperationLog> findAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param operationLog 实例对象
     * @return 实例对象
     */
    OperationLog insert(OperationLog operationLog);

    /**
     * 修改数据
     *
     * @param operationLog 实例对象
     * @return 实例对象
     */
    OperationLog update(OperationLog operationLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}