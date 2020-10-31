package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 远程调用者账号，用于验证调用是否合法(ApplicationRpcAccount)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:23
 */
@Data
public class ApplicationRpcAccount implements Serializable {
    private static final long serialVersionUID = -63214302854488359L;
    /**
     * ID
     */
    private Long id;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 凭证(密码)
     */
    private String identifier;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


}