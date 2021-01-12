package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 应用序列 新增 dto
 *
 * @author gonnaup
 * @version 2021/1/11 21:15
 */
@Data
@Validated
public class ApplicationSequenceDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 8560790995745241790L;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 序列类型
     */
    @NotNull(message = "序列号类型不能为空")
    private String sequenceType;
    /**
     * 序列号
     */
    @Min(value = 0, message = "序列号不能小于0")
    private int sequence;
    /**
     * 序列号步幅
     */
    @Min(value = 0, message = "序列号步幅不能小于0")
    private int step;



    public ApplicationSequence toApplicationSequence() {
        ApplicationSequence applicationSequence = new ApplicationSequence();
        BeanUtils.copyProperties(this, applicationSequence);
        return applicationSequence;
    }

}
