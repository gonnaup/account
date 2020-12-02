package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.enums.AuthType;
import org.gonnaup.accountmanagement.entity.Authentication;

import java.util.Optional;

/**
 * 账户认证信息(Authentication)表服务接口
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
public interface AuthenticationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Optional<Authentication> findById(Long id);

    /**
     * 在一个应用中email是否已经绑定账号
     * @param applicationName
     * @param email
     * @return <code>true</code>：已绑定；<code>false</code>：未绑定
     */
    default boolean emailBinded(String applicationName, String email) {
        return findOne(applicationName, AuthType.E.name(), email).isPresent();
    }

    /**
     * 查询单个认证信息
     * @param applicationName 应用名
     * @param authType 认证类型{@link org.gonnaup.account.enums.AuthType}
     * @param identifier 认证标的
     * @return 认证信息 <code>Optional</code>
     */
    Optional<Authentication> findOne(String applicationName, String authType, String identifier);

    /**
     * 新增数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    Authentication insert(Authentication authentication);

    /**
     * 修改数据
     *
     * @param authentication 实例对象
     * @return 实例对象
     */
    Authentication update(Authentication authentication);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}