package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.gonnaup.account.domain.RoleTree;
import org.gonnaup.accountmanagement.dao.AccountRoleDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.entity.Role;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.AccountRoleService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.RolePermissionService;
import org.gonnaup.accountmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 账户权限表(AccountRole)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
@Slf4j
@Service("accountRoleService")
@CacheConfig(cacheNames = {"accountRole"})
public class AccountRoleServiceImpl implements AccountRoleService {

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询账户的角色信息，可用于前台展示
     *
     * @param accountId 主键
     * @return 实例对象
     */
    @Override
    @Cacheable(key = "#accountId")
    public List<Role> findRolesByAccountId(Long accountId) {
        return accountRoleDao.queryRolesByAccountId(accountId);
    }

    /**
     * 查询账户所有角色名，用于鉴权
     *
     * @param accountId
     * @return 角色名列表
     */
    @Override
    @Cacheable(key = "'roleName' + #accountId")
    public List<String> findRoleNamesByAccountId(Long accountId) {
        return accountRoleDao.queryRoleNamesByAccountId(accountId);
    }

    /**
     * 查询账户的角色树，用于鉴权<br/>
     * <p>
     * 涉及角色树缓存删除的方法很多
     *     <ul>
     *         <li>{@link #deleteByAccountId(Long, String, Operater)} 删除某账户所有关联权限时，清除此账户角色树缓存</li>
     *         <li>{@link #deleteMany(Long, List, String, Operater)} 删除某账户的多个权限时，清除此账户角色树缓存</li>
     *         <li>{@link RolePermissionService#deleteByRoleId(Long, Operater)} 删除角色关联的所有权限时，清除所有角色树缓存</li>
     *         <li>{@link RolePermissionService#deleteMany(List, Operater)} 删除多组角色关联权限时，清除所有角色树缓存</li>
     *     </ul>
     * </p>
     *
     * @param accountId 账户名
     * @param appName   用于控制不同应用间缓存块
     * @return 角色树列表
     */
    @Override
    @Cacheable(cacheNames = "roleTree", key = "#appName + '$' +#accountId")
    public List<RoleTree> findRoleTreesByAccountId(Long accountId, String appName) {
        List<Role> roleList = accountRoleDao.queryRolesByAccountId(accountId);
        return roleList.parallelStream()
                .map(role -> {
                    RoleTree roleTree = new RoleTree();
                    roleTree.setRoleName(role.getRoleName());
                    roleTree.setPermissionNameSet(rolePermissionService.findPermissionNamesByRoleId(role.getId()));
                    return roleTree;
                }).collect(Collectors.toList());
    }

    /**
     * 批量新增数据,只允许添加某一账号的多个角色
     *
     * @param accountRoleList 实例对象组
     * @param operater        操作者
     * @return 实例对象
     */
    @Override
    @Transactional
    public List<AccountRole> insertBatch(List<AccountRole> accountRoleList, Operater operater) {
        if (CollectionUtils.isNotEmpty(accountRoleList)) {
            accountRoleDao.insertBatch(accountRoleList);
            String accountroleInfo = accountRoleList.toString();
            log.info("[{}] 批量插入账户角色 {}", operater.getOperaterId(), accountroleInfo);

            operationLogService.insert(OperationLog.of(operater, OperateType.A, "批量新增账户角色：" + accountroleInfo));
        }
        return accountRoleList;
    }

    /**
     * 删除账户所有角色
     *
     * @param accountId 账户ID
     * @param operater  操作者
     * @return 是否成功
     */
    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#accountId"),
            @CacheEvict(key = "'roleName' + #accountId"),
            @CacheEvict(cacheNames = "roleTree", key = "#appName + '$' +#accountId")})
    public boolean deleteByAccountId(Long accountId, String appName, Operater operater) {
        //先查询出要删除的角色，用作操作日志记录
        List<Role> roles = findRolesByAccountId(accountId);
        if (!roles.isEmpty()) {
            List<String> rolenames = roles.stream().map(Role::getRoleName).collect(Collectors.toList());
            accountRoleDao.deleteByAccountId(accountId);
            String rolenamesInfo = rolenames.toString();
            log.info("[{}] 解除账户id=[{}] 的所有角色 {}", operater.getOperaterId(), accountId, rolenamesInfo);

            //数据库日志
            operationLogService.insert(OperationLog.of(operater, OperateType.D, String.format("批量解除账户[%s]的关联角色：%s", accountId, rolenamesInfo)));
        }
        return true;
    }

    /**
     * 删除某账户的多个角色
     *
     * @param accountId 账户ID
     * @param roles     角色ID列表
     * @param operater  操作者
     * @return 删除个数
     */
    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#accountId", condition = "#result > 0"),
            @CacheEvict(key = "'roleName' + #accountId", condition = "#result > 0"),
            @CacheEvict(cacheNames = "roleTree", key = "#appName + '$' +#accountId", condition = "#result > 0")})
    public int deleteMany(Long accountId, List<Long> roles, String appName, Operater operater) {
        if (CollectionUtils.isNotEmpty(roles)) {
            // 删除关联关系
            //IO密集型使用并行流
            List<String> roleNameList = roles.parallelStream().map(roleId -> {
                Role role = roleService.findById(roleId);
                if (role != null) {
                    //删除关联关系
                    accountRoleDao.deleteByAccountIdAndRoleId(accountId, roleId);
                    //返回角色名
                    return role.getRoleName();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            String roleNames = roleNameList.toString();
            log.info("[{}] 批量解除账户角色 => 账户[{}] - 角色列表{} ", operater.getOperaterId(), accountId, roleNames);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "批量解除账户角色 => 账户[" + accountId + "] - 角色列表" + roleNames));
            return roleNameList.size();
        }
        return 0;
    }
}