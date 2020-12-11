package org.gonnaup.accountmanagement.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.springframework.beans.BeanUtils;

/** 操作日志view object
 * @author hy
 * @version 1.0
 * @Created on 2020/12/4 13:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperationLogVO extends OperationLog {

    private static final long serialVersionUID = 8865177325089656593L;

    /**
     * 字符串形式的ID
     * 防止前端长整型丢失精度
     */
    private String sid;

    public static OperationLogVO build(OperationLog operationLog) {
        OperationLogVO vo = new OperationLogVO();
        BeanUtils.copyProperties(operationLog, vo);
        vo.setSid(operationLog.getId().toString());
        vo.setOperaterType(OperaterType.valueOf(vo.getOperaterType()).description());
        vo.setOperateType(OperateType.valueOf(vo.getOperateType()).description());
        return vo;
    }

}
