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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限关联表(RolePermission)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
@Slf4j
@Service("rolePermissionService")
@CacheConfig(cacheNames = {"rolePermission"})
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
    @Cacheable(key = "#roleId")
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        return rolePermissionDao.queryPermissionsByRoleId(roleId);
    }

    /**
     * 查询角色所有权限名
     *
     * @param roleId 角色ID
     * @return 权限名列表
     */
    @Override
    @Cacheable(key = "'permissionName' + #roleId")
    public Set<String> findPermissionNamesByRoleId(Long roleId) {
        return rolePermissionDao.queryPermissionNamesByRoleId(roleId);
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
            String rolepermissionInfo = rolePermissionList.toString();
            log.info("[{}] 批量插入角色关联权限 {}", operater.getOperaterId(), rolepermissionInfo);

            operationLogService.insert(OperationLog.of(operater, OperateType.A, "新增角色关联权限：" + rolepermissionInfo));
        }
        return rolePermissionList;
    }

    /**
     * 解除角色所有权限
     *
     * @param roleId   角色id
     * @param appName  角色所属app
     * @param operater 操作者
     * @return 是否成功
     */
    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#roleId"),
            @CacheEvict(key = "'permissionName' + #roleId"),
            @CacheEvict(cacheNames = "roleTree", allEntries = true, condition = "#result")})
    public boolean deleteByRoleId(Long roleId, String appName, Operater operater) {
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
     * 解除某个角色的多个权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID
     * @param appName       角色所属app
     * @param operater      操作者
     * @return 删除个数
     */
    @Override
    @Transactional
    //todo 自定义实现 key支持"*"来删除某app下的角色树cache
    @Caching(evict = {@CacheEvict(key = "#roleId"),
            @CacheEvict(key = "'permissionName' + #roleId", condition = "#result > 0"),
            @CacheEvict(cacheNames = "roleTree", allEntries = true, condition = "#result > 0")})
    public int deleteMany(Long roleId, List<Long> permissionIds, String appName, Operater operater) {
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            List<String> permissionNameList = permissionIds.parallelStream().map(permissionId -> {
                Permission permission = permissionService.findById(permissionId);
                if (permission != null) {
                    //删除角色的权限
                    rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);
                    return permission.getPermissionName();//收集权限名，用来记录日志
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            String permissionNames = permissionNameList.toString();
            log.info("[{}] 批量解除角色关联权限 => 角色id[{}] - 权限名称{}", operater.getOperaterId(), roleId, permissionNames);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "批量解除角色关联权限 => 角色id[" + roleId + " - 权限名称" + permissionNames));
            return permissionNameList.size();
        }
        return 0;
    }
}