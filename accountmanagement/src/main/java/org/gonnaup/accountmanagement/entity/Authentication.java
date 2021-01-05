package org.gonnaup.accountmanagement.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * 账户认证信息(Authentication)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
@Getter
@Setter
@EqualsAndHashCode
public class Authentication implements Serializable {
    private static final long serialVersionUID = -97124320447210920L;
    /**
     * ID
     */
    private Long id;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 认证类型
     * P-密码
     * E-邮箱
     * W-微信
     * Q-QQ
     * B-微博
     */
    private String authType;

    /**
     * 唯一标识(用户名，
     * 邮箱或第三方应用
     * 的唯一标识)
     */
    private String identifier;

    /**
     * 凭证(密码或第三方token)
     */
    private String credential;

    /**
     * 过期时间，OAuth2登录时使用
     */
    private Long expires;


    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


    @Override
    public String toString() {
        return new StringJoiner(", ", Authentication.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("accountId=" + accountId)
                .add("applicationName='" + applicationName + "'")
                .add("authType='" + authType + "'")
                .add("identifier='" + identifier + "'")
                .toString();
    }
}