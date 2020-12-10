package org.gonnaup.account.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户信息(Account)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:17
 */
@Data
public class Account implements Serializable {
    private static final long serialVersionUID = -15389877065815411L;
    /**
     * ID
     */
    private Long id;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 用户名，同一应用中唯一
     */
    private String accountName;
    /**
     * 昵称
     */
    private String accountNickname;
    /**
     * 头像
     */
    private String accountAvatar;
    /**
     * 账户状态
     * N - 正常
     * F - 禁用
     */
    private String accountState;
    /**
     * 最近成功登陆时间，可用于清除僵尸账户
     */
    private LocalDateTime lastLogintime;
    /**
     * 账户标记，管理员使用
     */
    private String tag;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


}