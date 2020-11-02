package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 应用序列(ApplicationSequence)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:24
 */
@Data
public class ApplicationSequence implements Serializable {
    private static final long serialVersionUID = -89012557809334620L;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 序列类型
     */
    private String sequenceType;
    /**
     * 序列号
     */
    private int sequence;
    /**
     * 序列号间隔
     */
    private int step;


}