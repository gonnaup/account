package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账户角色表(Role)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:30
 */
@Data
public class Role implements Serializable {
    private static final long serialVersionUID = -71553339623120133L;
    /**
     * ID
     */
    private Long id;
    /**
     * 所属服务(为不同服务定制不同角色)
     */
    private String applicationName;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 角色描述
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

    /**
     * 包含权限列表
     */
    private List<Permission> permissions;

}