package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.domain.RoleTree;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.Role;

import java.util.List;

/**
 * 账户权限表(AccountRole)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
public interface AccountRoleService {

    /**
     * 查询账户的角色信息，可用于前台展示
     *
     * @param accountId 主键
     * @return 实例对象
     */
    List<Role> findRolesByAccountId(Long accountId);

    /**
     * 查询账户所有角色名，用于鉴权
     *
     * @param accountId
     * @return 角色名列表
     */
    List<String> findRoleNamesByAccountId(Long accountId);

    /**
     * 查询账户的角色树，用于鉴权
     *
     * @param accountId 账户ID
     * @param appName 应用名称，用于控制缓存
     * @return 角色树列表
     */
    List<RoleTree> findRoleTreesByAccountId(Long accountId, String appName);


    /**
     * 批量新增数据
     *
     * @param accountRoleList 实例对象组
     * @param operater        操作者
     * @return 实例对象
     */
    List<AccountRole> insertBatch(List<AccountRole> accountRoleList, Operater operater);

    /**
     * 删除账户所有角色
     *
     * @param accountId 账户ID
     * @param appName 账户所属app
     * @param operater  操作者
     * @return 是否成功
     */
    boolean deleteByAccountId(Long accountId, String appName, Operater operater);

    /**
     * 删除某账户的多个角色
     *
     * @param accountId 账户ID
     * @param roles     角色ID列表
     * @param appName 账户所属app
     * @param operater  操作者
     * @return 删除个数
     */
    int deleteMany(Long accountId, List<Long> roles, String appName, Operater operater);

}