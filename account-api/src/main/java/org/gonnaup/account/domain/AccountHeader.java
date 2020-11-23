package org.gonnaup.account.domain;

import lombok.Data;

import java.io.Serializable;

/** 账户核心信息
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/1 20:08
 */
@Data
public class AccountHeader implements Serializable {

    private static final long serialVersionUID = -5648343139832680637L;

    /**
     * 账户id
     */
    private Long id;

    /**
     * 账户名称，唯一
     */
    private String accountName;

    /**
     * 昵称
     */
    private String accountNickname;

    /**
     * 头像
     */
    private byte[] accountAvatar;

}
