package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.constant.ValidateGroups;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.validation.annotation.Validated;

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
@Validated
public class RoleDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = -2942742154740108870L;

    @NotNull(message = "角色ID不能为空", groups = ValidateGroups.UPDATE.class)
    private Long id;
    /**
     * 所属服务(为不同服务定制不同角色)
     */
    private String applicationName;
    /**
     * 角色名
     */
    @NotNull(message = "权限名不能为空", groups = ValidateGroups.ADD.class)
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
    List<Long> permissionIdList;

    public Role toRole() {
        Role role = new Role();
        BeanFieldCopyUtil.copyProperties(this, role);
        return role;
    }

}
