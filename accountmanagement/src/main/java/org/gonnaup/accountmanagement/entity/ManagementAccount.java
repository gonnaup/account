package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统管理员账号(ManagementAccount)实体类
 * 此账号用于管理此系统中的数据，分为两种
 *  A - 系统管理员，拥有所有权限
 *  S - 分为调用者管理员， 只能管理自身服务所属数据
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:26
 */
@Data
public class ManagementAccount implements Serializable {
    private static final long serialVersionUID = -90326186059263283L;
    /**
     * ID
     */
    private Long id;
    /**
     * 账号
     */
    private String managementName;
    /**
     * 管理员类型
     * A-系统管理员，拥有所有权限
     * S-服务调用者管理员，可以管理本服务的角色权限信息，由管理员创建
     */
    private String managementType;
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