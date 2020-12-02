package org.gonnaup.accountmanagement.dao;

import org.apache.ibatis.annotations.Param;
import org.gonnaup.accountmanagement.entity.Authentication;
import org.springframework.stereotype.Repository;

/**
 * 账户认证信息(Authentication)表数据库访问层
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
@Repository
public interface AuthenticationDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Authentication queryById(Long id);

    /**
     * 查询账户的某类型认证信息
     * @param accountId 账户id
     * @param AuthType 认证类型
     * @return 认证对象
     */
    Authentication queryByAccountIdAndAuthType(@Param("accountId") Long accountId, @Param("authType") String authType);

    /**
     * 按条件查询单个认证信息（唯一索引）
     * @param applicationName 应用名
     * @param authType 认证类型
     * @param identifier 认证标的
     * @return 认证对象
     */
    Authentication queryByApplicationnameAndAuthtypeAndIdentifier(@Param("applicationName") String applicationName, @Param("authType") String authType, @Param("identifier") String identifier);


    /**
     * 新增数据
     *
     * @param authentication 实例对象
     * @return 影响行数
     */
    int insert(Authentication authentication);

    /**
     * 修改数据
     *
     * @param authentication 实例对象
     * @return 影响行数
     */
    int update(Authentication authentication);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 删除账户的某类型认证信息
     * @param accountId
     * @param authType
     * @return
     */
    int deleteByAccountIdAndAuthType(@Param("accountId") Long accountId, @Param("authType") String authType);

}