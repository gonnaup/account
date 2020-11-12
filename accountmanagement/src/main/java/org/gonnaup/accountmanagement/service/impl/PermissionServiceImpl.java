package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.constant.AppSequenceKey;
import org.gonnaup.accountmanagement.dao.PermissionDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.ApplicationSequenceService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色权限表(Permission)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:29
 */
@Service("permissionService")
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

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
    public Permission findById(Long id) {
        return this.permissionDao.queryById(id);
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
    public Permission update(Permission permission, Operater operater) {
        int updateCount = permissionDao.update(permission);
        if (updateCount > 0) {
            operationLogService.insert(OperationLog.of(operater, OperateType.U, "更新权限对象：" + permission));
            if (log.isDebugEnabled()) {
                log.debug("更新权限对象 原对象：{} => 更新后对象：{}", findById(permission.getId()), permission);
            }
        }
        return permission;
    }

    /**
     * 通过主键删除数据
     *
     * @param id       主键
     * @param operater 操作者
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id, Operater operater) {
        Permission origin = findById(id);
        if (origin != null) {
            permissionDao.deleteById(id);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "删除权限对象：" + origin));
            log.info("删除权限对象 {}", origin);
            return true;
        }
        return false;
    }

}