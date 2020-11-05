package org.gonnaup.accountmanagement.service;

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
     * 新增数据
     *
     * @param accountRole 实例对象
     * @return 实例对象
     */
    AccountRole insert(AccountRole accountRole);

    /**
     * 删除账户所有角色
     *
     * @param accountId 主键
     * @return 是否成功
     */
    boolean deleteByAccountId(Long accountId);

    /**
     * 删除账户单个角色
     * @param accountId
     * @param roleId
     * @return
     */
    boolean deleteByAccountIdAndRoleId(Long accountId, Long roleId);

}