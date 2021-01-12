package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 角色新增dto
 *
 * @author gonnaup
 * @version 2021/1/12 11:20
 */
@Data
public class RoleDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = -2942742154740108870L;
    /**
     * 所属服务(为不同服务定制不同角色)
     */
    private String applicationName;
    /**
     * 角色名
     */
    @NotNull(message = "权限名不能为空")
    private String roleName;
    /**
     * 权限分数
     */
    @Pattern(regexp = "^[0-9a-fA-F]{1,8}$", message = "权重必须为1~8位的16进制数")
    private String score;
    /**
     * 角色描述
     */
    private String description;

    /**
     * 权限id列表
     */
    List<String> permissionIdList;

    public Role toRole() {
        Role role = new Role();
        BeanUtils.copyProperties(this, role);
        return role;
    }

}
