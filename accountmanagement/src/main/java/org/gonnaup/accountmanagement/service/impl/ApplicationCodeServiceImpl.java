package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.ApplicationCodeDao;
import org.gonnaup.accountmanagement.entity.ApplicationCode;
import org.gonnaup.accountmanagement.service.ApplicationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用代码(ApplicationCode)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:22
 */
@Service("applicationCodeService")
public class ApplicationCodeServiceImpl implements ApplicationCodeService {
    @Autowired
    private ApplicationCodeDao applicationCodeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param applicationName 主键
     * @return 实例对象
     */
    @Override
    public ApplicationCode queryById(String applicationName) {
        return this.applicationCodeDao.queryById(applicationName);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ApplicationCode> queryAllByLimit(int offset, int limit) {
        return this.applicationCodeDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param applicationCode 实例对象
     * @return 实例对象
     */
    @Override
    public ApplicationCode insert(ApplicationCode applicationCode) {
        this.applicationCodeDao.insert(applicationCode);
        return applicationCode;
    }

    /**
     * 修改数据
     *
     * @param applicationCode 实例对象
     * @return 实例对象
     */
    @Override
    public ApplicationCode update(ApplicationCode applicationCode) {
        this.applicationCodeDao.update(applicationCode);
        return this.queryById(applicationCode.getApplicationName());
    }

    /**
     * 通过主键删除数据
     *
     * @param applicationName 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String applicationName) {
        return this.applicationCodeDao.deleteById(applicationName) > 0;
    }
}