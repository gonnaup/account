package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Permission;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * permission 新增  dto
 *
 * @author gonnaup
 * @version 2021/1/11 21:19
 * @see Permission
 */
@Data
@Validated
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1779655911654906126L;
    /**
     * 所属服务(为不同服务定制不同角色)
     */
    private String applicationName;
    /**
     * 权限名称
     */
    @NotNull(message = "权限名不能为空")
    private String permissionName;
    /**
     * 权重
     */
    @Pattern(regexp = "^[0-9a-fA-F]{1,8}$", message = "权重必须为1~8位的16进制数")
    private String weight;
    /**
     * 权限描述
     */
    private String description;

    public Permission toPermission() {
        Permission permission = new Permission();
        BeanUtils.copyProperties(this, permission);
        return permission;
    }

}
