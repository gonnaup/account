package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.Role;
import org.gonnaup.account.exception.RelatedDataExistsException;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.AccountRoleDao;
import org.gonnaup.accountmanagement.dao.RoleDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.RolePermissionService;
import org.gonnaup.accountmanagement.service.RoleService;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 账户角色表(Role)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:30
 */
@Slf4j
@Service("roleService")
@CacheConfig(cacheNames = {"role"})
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Autowired
    private ApplicationSequenceService applicationSequenceService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    @Cacheable(key = "#id")
    public Role findById(Long id) {
        return this.roleDao.queryById(id);
    }

    /**
     * 根据应用名和角色名查询角色信息
     *
     * @param applicationName 应用名
     * @param roleName        角色名
     * @return
     */
    @Override
    @Cacheable(key = "'app&roleName::' + #applicationName + '_' + #roleName")
    public Role findByApplicationNameAndRoleName(String applicationName, String roleName) {
        return roleDao.queryByAccountName(applicationName, roleName);
    }

    /**
     * 查询多条数据
     *
     * @param role     条件
     * @param pageable 分页条件
     * @return 对象页
     */
    @Override
    public Page<Role> findAllConditionalPaged(Role role, Pageable pageable) {
        role = Optional.ofNullable(role).orElse(new Role()); //为空时赋值为空对象
        List<Role> roleList = roleDao.queryAllConditionalByLimit(role, pageable.getOffset(), pageable.getSize());
        int total = roleDao.countAllConditional(role);
        return Page.of(roleList, total);
    }

    /**
     * 查询某应用中所有角色对象
     *
     * @param appName
     * @return
     */
    @Override
    public List<Role> findByAppName(String appName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 新增数据
     *
     * @param role     实例对象
     * @param operater 操作者
     * @return 实例对象
     */
    @Override
    @Transactional
    public Role insert(Role role, Operater operater) {
        role.setId(applicationSequenceService.produceSequence(AppSequenceKey.ROLE_PERMISSION));
        this.roleDao.insert(role);
        log.info("{}[{}] 新增角色 {}", operater.getOperaterId(), operater.getOperaterName(), role);
        operationLogService.insert(OperationLog.of(operater, OperateType.A, "新增角色：" + role));
        return role;
    }


    /**
     * 修改数据
     *
     * @param role     实例对象
     * @param operater 操作者
     * @return 实例后对象
     */
    @Override
    @Transactional
    @Caching(put = {@CachePut(key = "#result.id", condition = "#result != null "),
            @CachePut(key = "'app&roleName::' + #result.applicationName + '_' + #result.roleName", condition = "#result != null ")})
    public Role update(Role role, Operater operater) {
        Role origin = findById(role.getId());
        if (origin == null) {
            log.error("要更新的角色 {} 不存在", role);
            return null;
        }
        this.roleDao.update(role);
        Role after = findById(role.getId());
        operationLogService.insert(OperationLog.of(operater, OperateType.U, String.format("更新角色：%s => %s", origin, after)));
        log.info("{}[{}] 修改角色信息 {} => {}", operater.getOperaterId(), operater.getOperaterName(), origin, after);
        return after;
    }

    /**
     * 通过主键删除数据
     *
     * @param id       主键
     * @param operater 操作者
     * @return 是否成功
     */
    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#result.id", condition = "#result != null "),
            @CacheEvict(key = "'app&roleName::' + #result.applicationName + '_' + #result.roleName", condition = "#result != null ")})
    public Role deleteById(Long id, Operater operater) {
        //是否存在关联账户数据
        int relatedAccount = accountRoleDao.countRoleRelated(id);
        if (relatedAccount > 0) {//存在账户数据关联
            log.warn("角色id[{}]存在[{}]个账户与之关联，无法删除此角色", id, relatedAccount);
            throw new RelatedDataExistsException("角色存在账户关联数据，请先删除关联数据!");
        }
        //删除逻辑
        Role origin = findById(id);
        if (origin != null) {
            roleDao.deleteById(id);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "删除角色：" + origin));
            log.info("{}[{}] 删除角色信息 {}", operater.getOperaterId(), operater.getOperaterName(), origin);
            //删除与权限的关联关系
            rolePermissionService.deleteByRoleId(id, origin.getApplicationName(), operater);
            return origin;
        }
        return null;
    }
}