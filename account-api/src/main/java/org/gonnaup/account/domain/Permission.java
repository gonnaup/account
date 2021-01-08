package org.gonnaup.account.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色权限表(Permission)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:28
 */
@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = 115683336132406994L;
    /**
     * ID
     */
    private Long id;
    /**
     * 所属服务(为不同服务定制不同角色)
     */
    private String applicationName;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权重
     */
    private String weight;
    /**
     * 权限描述
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;

}