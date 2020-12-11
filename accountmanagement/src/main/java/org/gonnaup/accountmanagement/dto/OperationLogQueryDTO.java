package org.gonnaup.accountmanagement.dto;

import lombok.Data;

import java.io.Serializable;

/**操作日志查询dto
 * @author hy
 * @version 2020/12/11 10:53
 */
@Data
public class OperationLogQueryDTO implements Serializable {

    private static final long serialVersionUID = -5569593604484599603L;
    /**
     * 操作人员类型 {@link org.gonnaup.accountmanagement.enums.OperaterType}
     * A-系统管理员(一般为维护应用信息和角色关联信息)
     * S-服务调用方(一般为创建用户)
     */
    private String operaterType;

    /**
     * 操作者名称
     */
    private String operaterName;

    /**
     * 操作类型 {@link org.gonnaup.accountmanagement.enums.OperateType}
     * A-添加操作
     * U-更新操作
     * D-删除操作
     */
    private String operateType;

    /**
     * 操作内容细节
     */
    private String operateDetail;

}
