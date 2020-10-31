package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色权限关联表(RolePermission)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:31
 */
@Data
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 369953413575768334L;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


}