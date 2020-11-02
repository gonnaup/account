package org.gonnaup.accountmanagement.entity;

import lombok.Data;

/**
 * 序列核心信息
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/2 10:21
 */
@Data
public class ApplicationSequenceHeader {

    /**
     * 序列号
     */
    private int sequence;
    /**
     * 序列号间隔
     */
    private int step;

}
