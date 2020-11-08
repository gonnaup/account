package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.ApplicationRpcAccountDao;
import org.gonnaup.accountmanagement.entity.ApplicationRpcAccount;
import org.gonnaup.accountmanagement.service.ApplicationRpcAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 远程调用者账号，用于验证调用是否合法(ApplicationRpcAccount)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:23
 */
@Service("applicationRpcAccountService")
public class ApplicationRpcAccountServiceImpl implements ApplicationRpcAccountService {
    @Autowired
    private ApplicationRpcAccountDao applicationRpcAccountDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ApplicationRpcAccount findById(Long id) {
        return this.applicationRpcAccountDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ApplicationRpcAccount> findAllByLimit(int offset, int limit) {
        return this.applicationRpcAccountDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param applicationRpcAccount 实例对象
     * @return 实例对象
     */
    @Override
    public ApplicationRpcAccount insert(ApplicationRpcAccount applicationRpcAccount) {
        this.applicationRpcAccountDao.insert(applicationRpcAccount);
        return applicationRpcAccount;
    }

    /**
     * 修改数据
     *
     * @param applicationRpcAccount 实例对象
     * @return 实例对象
     */
    @Override
    public ApplicationRpcAccount update(ApplicationRpcAccount applicationRpcAccount) {
        this.applicationRpcAccountDao.update(applicationRpcAccount);
        return this.findById(applicationRpcAccount.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.applicationRpcAccountDao.deleteById(id) > 0;
    }
}