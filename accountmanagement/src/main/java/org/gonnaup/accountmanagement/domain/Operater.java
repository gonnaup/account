package org.gonnaup.accountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gonnaup.accountmanagement.enums.OperaterType;

/**
 * 业务操作者信息
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/11 15:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Operater {

    /**
     * 操作员类型
     */
    private OperaterType operaterType;

    /**
     * 操作员Id
     */
    private Long operaterId;

    /**
     * 操作员名称
     */
    private String operaterName;


}
