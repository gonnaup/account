package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.AccountHeader;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户信息(Account)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:18
 */
@Repository
public interface AccountDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Account queryById(Long id);

    /**
     * 通过ID查询账户核心信息
     * @param id
     * @return
     */
    AccountHeader queryHeaderById(Long id);

    /**
     * 根据应用名称和账号名查询账号核心信息
     * @param applicationName 应用名称
     * @param accountName 账号名
     * @return 账号核心信息，没有则返回 <code>null</code>
     */
    AccountHeader queryHeaderByApplicationnameAndAccountname(@Param("applicationName") String applicationName, @Param("accountName") String accountName);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param account 实例对象
     * @return 对象列表
     */
    List<Account> queryAllConditionalByLimit(@Param("account") Account account, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 获取符合条件数据的总条数
     * @param account 条件
     * @return 总条数
     */
    int countAllConditional(@Param("account") Account account);

    /**
     * 某应用使用某个账户名的数据个数
     * @param applicationName
     * @param accountName
     * @return 数据个数
     */
    int accountNameExist(@Param("applicationName") String applicationName, @Param("accountName") String accountName);

    /**
     * 某应用使用某个昵称的数据数
     * @param applicationName
     * @param accountNickname
     * @return 数据个数
     */
    int accountNicknameExist(@Param("applicationName") String applicationName, @Param("accountNickname") String accountNickname);

    /**
     * 查询账户状态
     * @param id
     * @return 账户状态
     */
    String queryAccountStateById(Long id);

    /**
     * 新增数据
     *
     * @param account 实例对象
     * @return 影响行数
     */
    int insert(Account account);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Account> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Account> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Account> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<Account> entities);

    /**
     * 修改数据
     *
     * @param account 实例对象
     * @return 影响行数
     */
    int update(Account account);

    /**
     * 修改账户状态
     * @param id
     * @param accountState
     * @return 影响数据条数
     */
    int updateAccountStateById(@Param("id")Long id, @Param("accountState")String accountState);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}