package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户权限表(AccountRole)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:19
 */
@Data
public class AccountRole implements Serializable {
    private static final long serialVersionUID = -27925456995372772L;
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


}