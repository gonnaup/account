package org.gonnaup.accountmanagement.service;

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
     * 查询账户的角色信息
     *
     * @param accountId 主键
     * @return 实例对象
     */
    List<Role> findRolesByAccountId(Long accountId);


    /**
     * 批量新增数据
     *
     * @param accountRoleList 实例对象组
     * @param operater 操作者
     * @return 实例对象
     */
    List<AccountRole> insertBatch(List<AccountRole> accountRoleList, Operater operater);

    /**
     * 删除账户所有角色
     *
     * @param accountId 账户ID
     * @param operater 操作者
     * @return 是否成功
     */
    boolean deleteByAccountId(Long accountId, Operater operater);

    /**
     * 删除账户的多组角色
     * @param keys 账户角色列表
     * @param operater 操作者
     * @return 删除个数
     */
    int deleteMany(List<AccountRole> keys, Operater operater);

}