package org.gonnaup.accountmanagement.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用代码(ApplicationCode)实体类
 *
 * @author gonnaup
 * @since 2020-10-29 10:53:21
 */
@Data
public class ApplicationCode implements Serializable {
    private static final long serialVersionUID = 817615991392059128L;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 应用代码 100~999的数字
     */
    private Integer applicationCode;
    /**
     * 应用url
     */
    private String url;
    /**
     * 应用描述
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

}