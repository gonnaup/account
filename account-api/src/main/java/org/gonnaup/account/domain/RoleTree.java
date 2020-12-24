package org.gonnaup.account.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/** 角色-权限树，
 * @author gonnaup
 * @version 2020/12/21 21:13
 */
@Data
public class RoleTree implements Serializable {

    private static final long serialVersionUID = 4551477207777071229L;
    /**
     * 角色名
     */
    private String roleName;

    /**
     * 权限列表
     */
    private Set<String> permissionNameSet;

}
