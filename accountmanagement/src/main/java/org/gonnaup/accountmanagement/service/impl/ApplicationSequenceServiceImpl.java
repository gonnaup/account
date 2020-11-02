package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.ApplicationSequenceDao;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.service.ApplicationSequenceKey;
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
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ApplicationSequence> queryAllByLimit(int offset, int limit) {
        // todo
        return null;
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
     * 通过主键删除数据
     *
     * @param applicationSequenceKey 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(ApplicationSequenceKey applicationSequenceKey) {
        return this.applicationSequenceDao.deleteById(applicationSequenceKey.getApplicationName(), applicationSequenceKey.getSequenceType()) > 0;
    }
}