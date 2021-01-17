package org.gonnaup.accountmanagement.vo;

import lombok.Data;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * permission VO
 *
 * @author gonnaup
 * @version 2021/1/11 20:46
 */
@Data
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 4763327148905013882L;
    /**
     * ID
     */
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
     * 权重
     */
    private String weight;
    /**
     * 权限描述
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

    public static PermissionVO fromPermission(Permission permission) {
        PermissionVO permissionVO = new PermissionVO();
        BeanFieldCopyUtil.copyProperties(permission, permissionVO);
        permissionVO.setId(Long.toString(permission.getId()));
        return permissionVO;
    }

}
