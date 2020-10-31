package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户Token(AccountToken)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:20
 */
@Data
public class AccountToken implements Serializable {
    private static final long serialVersionUID = -41889594828791473L;
    /**
     * ID
     */
    private Long id;
    /**
     * 账户ID
     */
    private Long accountId;
    /**
     * token值
     */
    private String accountToken;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

}