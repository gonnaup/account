package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.ApplicationSequenceDao;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用序列(ApplicationSequence)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
@Service("applicationSequenceService")
public class ApplicationSequenceServiceImpl implements ApplicationSequenceService {
    @Autowired
    private ApplicationSequenceDao applicationSequenceDao;

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    @Override
    public ApplicationSequence queryById(String applicationName) {
        return this.applicationSequenceDao.queryById(applicationName);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ApplicationSequence> queryAllByLimit(int offset, int limit) {
        return this.applicationSequenceDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param applicationSequence 实例对象
     * @return 实例对象
     */
    @Override
    public ApplicationSequence insert(ApplicationSequence applicationSequence) {
        this.applicationSequenceDao.insert(applicationSequence);
        return applicationSequence;
    }

    /**
     * 修改数据
     *
     * @param applicationSequence 实例对象
     * @return 实例对象
     */
    @Override
    public ApplicationSequence update(ApplicationSequence applicationSequence) {
        this.applicationSequenceDao.update(applicationSequence);
        return this.queryById(applicationSequence.getApplicationName());
    }

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String applicationName) {
        return this.applicationSequenceDao.deleteById(applicationName) > 0;
    }
}