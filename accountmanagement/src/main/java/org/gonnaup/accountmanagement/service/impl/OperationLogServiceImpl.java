package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.OperationLogDao;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统管理员账号(OperationLog)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:28
 */
@Service("operationLogService")
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public OperationLog findById(Long id) {
        return this.operationLogDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<OperationLog> findAllByLimit(int offset, int limit) {
        return this.operationLogDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param operationLog 实例对象
     * @return 实例对象
     */
    @Override
    public OperationLog insert(OperationLog operationLog) {
        this.operationLogDao.insert(operationLog);
        return operationLog;
    }

    /**
     * 修改数据
     *
     * @param operationLog 实例对象
     * @return 实例对象
     */
    @Override
    public OperationLog update(OperationLog operationLog) {
        this.operationLogDao.update(operationLog);
        return this.findById(operationLog.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.operationLogDao.deleteById(id) > 0;
    }
}