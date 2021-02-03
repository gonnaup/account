package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.DataNotInitialized;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.OperationLogDao;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统管理员账号(OperationLog)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:28
 */
@Service("operationLogService")
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    @Autowired
    ApplicationSequenceService applicationSequenceService;

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
     * @param operationLog 条件对象, 为<code>null</code>时查询所有对象
     * @param pageable     分页
     * @return 对象列表
     */
    @Override
    public Page<OperationLog> findAllConditionalPaged(OperationLog operationLog, Pageable pageable) {
        operationLog = Optional.ofNullable(operationLog).orElse(new OperationLog());
        List<OperationLog> operationLogList = operationLogDao.queryAllCoditionalByLimit(operationLog, pageable.getOffset(), pageable.getSize());
        int total = operationLogDao.countAllConditional(operationLog);
        return Page.of(operationLogList, total);
    }

    /**
     * 新增数据
     *
     * @param operationLog 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public OperationLog insert(OperationLog operationLog) {
        try {
            operationLog.setId(applicationSequenceService.produceSequence(AppSequenceKey.OPERATIONLOG));
            this.operationLogDao.insert(operationLog);
            if (log.isDebugEnabled()) {
                log.debug("插入操作日志 {}", operationLog);
            }
        } catch (DataNotInitialized dataNotInitialized) {
            throw dataNotInitialized;
        } catch (Exception e) {
            log.error("插入操作日志 {} 失败，请通知管理员", operationLog);
            log.error(e.getMessage());
        }
        return operationLog;
    }

    /**
     * 修改数据
     *
     * @param operationLog 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public void update(OperationLog operationLog) {
        this.operationLogDao.update(operationLog);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return this.operationLogDao.deleteById(id) > 0;
    }
}