package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * role 查询 dto
 *
 * @author gonnaup
 * @version 2021/1/11 21:00
 */
@Data
@Validated
public class RoleQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 1057301803605390963L;
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
     * 角色名
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String description;

    public Role toRole() {
        Role role = new Role();
        BeanFieldCopyUtil.copyProperties(this, role);
        if (StringUtils.isNotBlank(id)) {
            role.setId(Long.parseLong(id));
        }
        return role;
    }

}
