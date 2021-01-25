package org.gonnaup.accountmanagement.service.impl;

import org.gonnaup.account.domain.Authentication;
import org.gonnaup.account.enums.AuthType;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.AuthenticationDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.AuthenticationService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ApplicationSequenceService applicationSequenceService;

    @Autowired
    private OperationLogService operationLogService;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Authentication findById(Long id) {
        return authenticationDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param authentication
     * @param pageable
     * @return
     */
    @Override
    public Page<Authentication> findAllConditionalPaged(Authentication authentication, Pageable pageable) {
        authentication = Optional.ofNullable(authentication).orElse(new Authentication());
        List<Authentication> authenticationList = authenticationDao.queryAllConditionalByLimit(authentication, pageable.getOffset(), pageable.getSize());
        int count = authenticationDao.countAllConditional(authentication);
        return Page.of(authenticationList, count);
    }

    /**
     * 查询单个认证信息
     *
     * @param applicationName 应用名
     * @param authType        认证类型{@link AuthType}
     * @param identifier      认证标的
     * @return 认证信息
     */
    @Override
    public Authentication findOne(String applicationName, String authType, String identifier) {
        return authenticationDao.queryByApplicationnameAndAuthtypeAndIdentifier(applicationName, authType, identifier);
    }

    /**
     * 查询账号的email认证信息
     *
     * @param accountId 账号id
     * @return email认证信息
     */
    @Override
    public Authentication findByAccountIdOfEmail(Long accountId) {
        return authenticationDao.queryByAccountIdAndAuthType(accountId, AuthType.E.name());
    }

    /**
     * 新增数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Authentication authentication) {
        long id = applicationSequenceService.produceSequence(AppSequenceKey.AUTH);
        authentication.setId(id);
        return this.authenticationDao.insert(authentication);
    }

    /**
     * 修改数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Authentication authentication) {
        return this.authenticationDao.update(authentication);
    }

    /**
     * 修改数据
     *
     * @param authentication
     * @param operater
     * @return
     */
    @Override
    public int update(Authentication authentication, Operater operater) {
        int count = authenticationDao.update(authentication);
        operationLogService.insert(OperationLog.of(operater, OperateType.U, "更新认证信息ID=" + authentication.getId()));
        return count;
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