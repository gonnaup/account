package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.accountmanagement.dao.AuthenticationDao;
import org.gonnaup.accountmanagement.entity.Authentication;
import org.gonnaup.accountmanagement.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户认证信息(Authentication)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private AuthenticationDao authenticationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Authentication queryById(Long id) {
        return this.authenticationDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Authentication> queryAllByLimit(int offset, int limit) {
        return this.authenticationDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    @Override
    public Authentication insert(Authentication authentication) {
        this.authenticationDao.insert(authentication);
        return authentication;
    }

    /**
     * 修改数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    @Override
    public Authentication update(Authentication authentication) {
        this.authenticationDao.update(authentication);
        return this.queryById(authentication.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.authenticationDao.deleteById(id) > 0;
    }
}