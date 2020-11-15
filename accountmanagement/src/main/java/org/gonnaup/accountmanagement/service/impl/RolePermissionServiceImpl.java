package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.gonnaup.accountmanagement.dao.RolePermissionDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.entity.Permission;
import org.gonnaup.accountmanagement.entity.RolePermission;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.PermissionService;
import org.gonnaup.accountmanagement.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 角色权限关联表(RolePermission)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
@Service("rolePermissionService")
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询某角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        return rolePermissionDao.queryPermissionsByRoleId(roleId);
    }

    /**
     * 批量插入
     *
     * @param rolePermissionList 对象列表
     * @param operater           操作者
     * @return
     */
    @Override
    @Transactional
    public List<RolePermission> insertBatch(List<RolePermission> rolePermissionList, Operater operater) {
        if (CollectionUtils.isNotEmpty(rolePermissionList)) {
            rolePermissionDao.insertBatch(rolePermissionList);
            log.info("[{}] 批量插入角色关联权限 {}", operater.getOperaterId(), rolePermissionList);

            operationLogService.insert(OperationLog.of(operater, OperateType.A, "新增角色关联权限：" + rolePermissionList.toString()));
        }
        return rolePermissionList;
    }

    /**
     * 解除角色所有权限
     *
     * @param roleId   角色id
     * @param operater 操作者
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteByRoleId(Long roleId, Operater operater) {
        //所有权限名
        List<String> permissionnames = findPermissionsByRoleId(roleId).stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toList());
        if (!permissionnames.isEmpty()) {
            rolePermissionDao.deleteByRoleId(roleId);
            log.info("[{}] 解除角色id=[{}] 的所有权限 {}", operater.getOperaterId(), roleId, permissionnames);

            operationLogService.insert(OperationLog.
                    of(operater, OperateType.D, String.format("批量解除角色[%s]的关联权限：%s", roleId, permissionnames)));
        }
        return true;
    }

    /**
     * 解除多个角色的权限
     *
     * @param keys     主键列表
     * @param operater 操作者
     * @return 删除个数
     */
    @Override
    @Transactional
    public int deleteMany(List<RolePermission> keys, Operater operater) {
        if (CollectionUtils.isNotEmpty(keys)) {
            //角色id - 解除的权限名称 进行分组收集
            Map<Long, List<String>> roleIdPermissionName = keys.stream().collect(Collectors.groupingBy(RolePermission::getRoleId,
                    Collector.of((Supplier<List<String>>) ArrayList::new, (list, rolePermission) -> {
                        Permission permission = permissionService.findById(rolePermission.getPermissionId());
                        if (permission != null) {
                            //删除角色的权限
                            rolePermissionDao.deleteByRoleIdAndPermissionId(rolePermission.getRoleId(), rolePermission.getPermissionId());
                            String permissionName = permission.getPermissionName();
                            log.info("[{}] 解除角色id=[{}] 的权限 {}", operater.getOperaterId(), rolePermission.getRoleId(), permissionName);
                            list.add(permissionName);//收集权限名，用来记录日志
                        }
                    }, (left, right) -> {
                        left.addAll(right);
                        return left;
                    })));

            operationLogService.insert(OperationLog.of(operater, OperateType.D, "批量解除角色关联权限(角色id - 权限名称)：" + roleIdPermissionName));
            return roleIdPermissionName.values().stream().mapToInt(s -> s.size()).sum();
        }
        return 0;
    }
}