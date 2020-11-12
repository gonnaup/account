package org.gonnaup.accountmanagement.entity;

import lombok.Data;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.enums.OperaterType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统操作日志(OperationLog)实体类
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

    public OperationLog() {}

    /**
     * 静态构建方法
     * @param operaterName 操作者名称
     * @param operaterId 操作者Id
     * @param operaterType 操作者类型
     * @param operateType 操作类型
     * @param operateDetail 操作细节
     * @return 操作日志对象
     */
    public static OperationLog of(String operaterName, Long operaterId, OperaterType operaterType, OperateType operateType, String operateDetail) {
        OperationLog operationLog = new OperationLog();
        operationLog.setOperaterName(operaterName);
        operationLog.setOperaterId(operaterId);
        operationLog.setOperaterType(operaterType.name());
        operationLog.setOperateType(operateType.name());
        operationLog.setOperateDetail(operateDetail);
        return operationLog;
    }

    public static OperationLog of(Operater operater, OperateType operateType, String operateDetail) {
        return of(operater.getOperaterName(), operater.getOperaterId(), operater.getOperaterType(), operateType, operateDetail);
    }


}