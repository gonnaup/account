package org.gonnaup.accountmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 选择框VO
 * @author gonnaup
 * @version 2021/1/10 11:40
 */
@Data
@AllArgsConstructor(staticName = "of")
public class SelectVO implements Serializable {

    private static final long serialVersionUID = -4411045696888083556L;

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;


}
