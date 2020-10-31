package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户认证信息(Authentication)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:25
 */
@Data
public class Authentication implements Serializable {
    private static final long serialVersionUID = -97124320447210920L;
    /**
     * ID
     */
    private Long id;

    private Long accountId;
    /**
     * 认证类型
     * A-账户
     * E-邮箱
     * W-微信
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
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


}