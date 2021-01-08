package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.RelatedDataExistsException;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.PermissionDao;
import org.gonnaup.accountmanagement.dao.RolePermissionDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色权限表(Permission)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:29
 */
@Slf4j
@Service("permissionService")
@CacheConfig(cacheNames = {"permission"})
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

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
    @Cacheable(key = "#id")
    public Permission findById(Long id) {
        return this.permissionDao.queryById(id);
    }

    /**
     * 根据应用名和权限名查询权限对象
     *
     * @param applicationName 应用名
     * @param permissionName  权限名
     * @return 权限对象
     */
    @Override
    @Cacheable(key = "'app&permissionName::' + #applicationName + '_' + #permissionName")
    public Permission findByApplicationNameAndPermissionName(String applicationName, String permissionName) {
        return permissionDao.queryByPermissionName(applicationName, permissionName);
    }

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @param operater   操作者
     * @return 实例对象
     */
    @Override
    public Permission insert(Permission permission, Operater operater) {
        permission.setId(applicationSequenceService.produceSequence(AppSequenceKey.ROLE_PERMISSION));
        permissionDao.insert(permission);
        log.info("添加权限对象 {}", permission);
        operationLogService.insert(OperationLog.of(operater, OperateType.A, "添加权限对象：" + permission));
        return permission;
    }

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @param operater   操作者
     * @return 实例对象
     */
    @Override
    @Transactional
    @Caching(put = {@CachePut(key = "#result.id", condition = "#result != null "),
            @CachePut(key = "'app&permissionName::' + #result.applicationName + '_' + #result.permissionName", condition = "#result != null ")})
    public Permission update(Permission permission, Operater operater) {
        Permission origin = findById(permission.getId());
        if (origin == null) {
            log.error("需要更新的权限对象 {} 不存在", permission);
            return null;
        }
        permissionDao.update(permission);
        Permission after = findById(permission.getId());
        operationLogService.insert(OperationLog.of(operater, OperateType.U, String.format("更新权限对象：%s => %s", origin, after)));
        log.info("{}[{}]更新权限对象 原对象：{} => 更新后对象：{}", operater.getOperaterId(), operater.getOperaterName(), origin, after);
        return after;
    }

    /**
     * 通过主键删除数据
     *
     * @param id       主键
     * @param operater 操作者
     * @return 删除的对象
     */
    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#result.id", condition = "#result != null "),
            @CacheEvict(key = "'app&permissionName::' + #result.applicationName + '_' + #result.permissionName", condition = "#result != null ")})
    public Permission deleteById(Long id, Operater operater) {
        //先判断是否存在关联数据(角色关联数据)
        int relatedCount = rolePermissionDao.countPermissionRelated(id);
        if (relatedCount > 0) {
            log.warn("权限id[{}]存在[{}]个角色与之关联，无法删除此角色!", id, relatedCount);
            throw new RelatedDataExistsException("权限存在角色关联数据，请先删除关联数据");
        }
        Permission origin = findById(id);
        if (origin != null) {
            permissionDao.deleteById(id);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "删除权限：" + origin));
            log.info("删除权限对象 {}", origin);
            return origin;
        }
        return null;
    }

}