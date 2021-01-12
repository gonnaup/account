package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.dao.ApplicationCodeDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.ApplicationCode;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationCodeService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 应用代码(ApplicationCode)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:22
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "applicationCode")
public class ApplicationCodeServiceImpl implements ApplicationCodeService {

    @Autowired
    private ApplicationCodeDao applicationCodeDao;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    @Override
    @Cacheable(key = "#applicationName")
    public ApplicationCode findByApplicationName(String applicationName) {
        return applicationCodeDao.queryById(applicationName);
    }

    /**
     * 查询多条数据
     *
     * @param applicationCode
     * @return 对象列表
     */
    @Override
    public List<ApplicationCode> findAllConditional(ApplicationCode applicationCode) {
        return applicationCodeDao.queryAllConditional(Optional.ofNullable(applicationCode).orElse(new ApplicationCode()));
    }

    /**
     * 新增应用数据，只有ADMIN才有权限
     *
     * @param applicationCode 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public ApplicationCode insert(ApplicationCode applicationCode, Operater operater) {
        applicationCodeDao.insert(applicationCode);
        log.info("[{}] 新增应用代码 {}", operater.getOperaterId(), applicationCode);
        operationLogService.insert(OperationLog.of(operater, OperateType.A, "新增应用编码：" + applicationCode));
        return applicationCode;
    }

    /**
     * 修改数据，只能更新code,url,description字段
     *
     * @param applicationCode 实例对象
     * @param operater        操作者
     * @return 实例对象
     */
    @Override
    @Transactional
    @CachePut(key = "#applicationCode.applicationName", condition = "#result != null")
    public ApplicationCode update(ApplicationCode applicationCode, Operater operater) {
        ApplicationCode origin = findByApplicationName(applicationCode.getApplicationName());
        if (origin == null) {
            log.error("需要更新的应用编码 {} 不存在", applicationCode);
            return null;
        }
        applicationCodeDao.update(applicationCode);
        ApplicationCode after = findByApplicationName(applicationCode.getApplicationName());
        operationLogService.insert(OperationLog.of(operater, OperateType.U, String.format("更新应用编码 %s => %s", origin, after)));
        log.info("{}[{}] 修改应用编码 {} => {}", operater.getOperaterId(), operater.getOperaterName(), origin, after);
        return after;
    }

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @param operater        操作者
     * @return 是否成功
     */
    @Override
    @Transactional
    @CacheEvict(key = "#applicationName", condition = "#result != null")
    public ApplicationCode deleteOne(String applicationName, Operater operater) {
        ApplicationCode origin = findByApplicationName(applicationName);
        if (origin == null) {
            log.warn("要删除的应用编码 应用名={} 不存在", applicationName);
            return null;
        }
        int count = applicationCodeDao.deleteById(applicationName);
        if (count > 0) {
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "删除应用编码：" + origin));
            log.info("{}[{}] 删除应用编码 {}", operater.getOperaterId(), operater.getOperaterName(), origin);
            return origin;
        }
        return null;
    }
}