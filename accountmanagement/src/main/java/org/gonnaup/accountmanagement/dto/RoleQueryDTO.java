package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * role 查询 dto
 *
 * @author gonnaup
 * @version 2021/1/11 21:00
 */
@Data
public class RoleQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 1057301803605390963L;
    /**
     * ID
     */
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
        BeanUtils.copyProperties(this, role);
        role.setId(Long.parseLong(id));
        return role;
    }

}
