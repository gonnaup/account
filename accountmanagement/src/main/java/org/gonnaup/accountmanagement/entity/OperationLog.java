package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统管理员账号(OperationLog)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:27
 */
@Data
public class OperationLog implements Serializable {
    private static final long serialVersionUID = -92008411702091167L;
    /**
     * ID
     */
    private Long id;
    /**
     * 操作人员类型
     * A-系统管理员(一般为维护应用信息和角色关联信息)
     * S-服务调用方(一般为创建用户)
     */
    private String operaterType;
    /**
     * 账号
     */
    private Long operaterId;
    /**
     * 操作者名称
     */
    private String operaterName;

    /**
     * 操作类型
     * A-添加操作
     * U-更新操作
     */
    private String operateType;

    /**
     * 操作内容细节
     */
    private String operateDetail;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;


}