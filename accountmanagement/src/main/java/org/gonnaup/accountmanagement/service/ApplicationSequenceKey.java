package org.gonnaup.accountmanagement.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 序列主键包装对象
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/2 11:07
 */
@Data
@AllArgsConstructor
public class ApplicationSequenceKey {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 序列类型
     */
    private String sequenceType;
}
