package org.gonnaup.accountmanagement.vo;

import lombok.Data;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * role VO
 *
 * @author gonnaup
 * @version 2021/1/11 20:50
 */
@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = -3615848761685530381L;
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
     * 权限分数
     */
    private String score;
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

    public static RoleVO fromRole(Role role) {
        RoleVO roleVO = new RoleVO();
        BeanFieldCopyUtil.copyProperties(role, roleVO);
        roleVO.setId(Long.toString(role.getId()));
        return roleVO;
    }

}
