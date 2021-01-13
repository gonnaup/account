package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * permission 查询 dto
 * @author gonnaup
 * @version 2021/1/11 20:55
 */
@Data
@Valid
public class PermissionQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 8984631031312392263L;
    /**
     * ID
     */
    @Pattern(regexp = "^[0-9]*$", message = "ID必须为数字")
    private String id;

    /**
     * 所属服务(为不同服务定制不同角色)
     */
    private String applicationName;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限描述
     */
    private String description;

    public Permission toPermission() {
        Permission permission = new Permission();
        BeanUtils.copyProperties(this, permission);
        if (StringUtils.isNotBlank(id)) {
            permission.setId(Long.parseLong(id));
        }
        return permission;
    }

}
