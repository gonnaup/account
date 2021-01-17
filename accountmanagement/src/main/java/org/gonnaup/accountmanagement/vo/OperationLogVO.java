package org.gonnaup.accountmanagement.vo;

import lombok.Data;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志view object
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/12/4 13:04
 */
@Data
public class OperationLogVO implements Serializable {

    private static final long serialVersionUID = 8865177325089656593L;

    /**
     * 字符串形式的ID
     * 防止前端长整型丢失精度
     */
    private String sid;
    /**
     * 操作人员类型 {@link org.gonnaup.accountmanagement.enums.OperaterType}
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

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    public static OperationLogVO build(OperationLog operationLog) {
        OperationLogVO vo = new OperationLogVO();
        BeanFieldCopyUtil.copyProperties(operationLog, vo);
        vo.setSid(operationLog.getId().toString());
        vo.setOperaterType(OperaterType.valueOf(vo.getOperaterType()).description());
        vo.setOperateType(OperateType.valueOf(vo.getOperateType()).description());
        return vo;
    }

}
