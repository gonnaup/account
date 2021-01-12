package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.accountmanagement.entity.ApplicationCode;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 应用编码DTO
 *
 * @author gonnaup
 * @version 2021/1/10 14:02
 */
@Data
@Validated
public class ApplicationCodeDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 9183045205818641885L;
    /**
     * 应用名称
     */
    @NotNull(message = "应用名不能为空")
    private String applicationName;
    /**
     * 应用代码 10~99的数字
     */
    @NotNull(message = "应用编码不能为空")
    @Max(value = 99, message = "应用编码应当在10~99之间")
    @Min(value = 10, message = "应用编码应当在10~99之间")
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
     * 对象转换
     *
     * @return
     */
    public ApplicationCode toApplicationCode() {
        ApplicationCode applicationCode = new ApplicationCode();
        BeanUtils.copyProperties(this, applicationCode);
        return applicationCode;
    }

}
