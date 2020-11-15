package org.gonnaup.accountmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.gonnaup.accountmanagement.dao.AccountRoleDao;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.entity.Role;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.service.AccountRoleService;
import org.gonnaup.accountmanagement.service.OperationLogService;
import org.gonnaup.accountmanagement.service.RoleService;
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
 * 账户权限表(AccountRole)表服务实现类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
@Service("accountRoleService")
@Slf4j
public class AccountRoleServiceImpl implements AccountRoleService {

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询账户的角色信息
     *
     * @param accountId 主键
     * @return 实例对象
     */
    @Override
    public List<Role> findRolesByAccountId(Long accountId) {
        return accountRoleDao.queryRolesByAccountId(accountId);
    }

    /**
     * 批量新增数据
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
    public boolean deleteByAccountId(Long accountId, Operater operater) {
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
     * 删除多组账户的角色
     *
     * @param keys     账户角色列表
     * @param operater 操作者
     * @return 删除个数
     */
    @Override
    @Transactional
    public int deleteMany(List<AccountRole> keys, Operater operater) {
        if (CollectionUtils.isNotEmpty(keys)) {
            // 删除关联关系
            // 查询出相关信息做记录 accountId - List<roleName>
            //IO密集型使用并行流
            Map<Long, List<String>> accountIdRoleName = keys.parallelStream().collect(Collectors.groupingBy(AccountRole::getAccountId,
                    Collector.of(
                            (Supplier<List<String>>) ArrayList::new, //容器对象
                            (list, accountRole) -> {  //accumulator 收集逻辑
                                Role role = roleService.findById(accountRole.getRoleId());//角色信息
                                if (role != null) {
                                    //删除
                                    accountRoleDao.deleteByAccountIdAndRoleId(accountRole.getAccountId(), accountRole.getRoleId());
                                    list.add(role.getRoleName());//收集角色名
                                }
                            },
                            (left, right) -> {    //combiner 连接逻辑，主要是并行时
                                left.addAll(right);
                                return left;
                            }
                    )));
            String accountIdRoleNameInfo = accountIdRoleName.toString();
            log.info("[{}] 批量解除账户角色(账户id - 角色名称) {}", operater.getOperaterId(), accountIdRoleNameInfo);
            operationLogService.insert(OperationLog.of(operater, OperateType.D, "批量解除账户角色(账户id - 角色名称)：" + accountIdRoleNameInfo));
            return accountIdRoleName.values().stream().mapToInt(List::size).sum();
        }
        return 0;
    }
}