package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;

import java.io.Serializable;

/**
 * 序列号查询dto
 *
 * @author gonnaup
 * @version 2021/1/11 11:56
 */
@Data
public class ApplicationSequenceQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = -4279799829984750683L;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 序列类型
     */
    private String sequenceType;

    public ApplicationSequenceKey toApplicationSequenceKey() {
        return ApplicationSequenceKey.of(applicationName, sequenceType);
    }

}
