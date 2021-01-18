package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Authentication;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 认证信息查询 dto
 * @author gonnaup
 * @version 2021/1/17 21:21
 */
@Data
@Valid
public class AuthenticationQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 3594464142324686957L;
    /**
     * ID
     */
    @Pattern(regexp = "^[0-9]*$", message = "ID必须为数字")
    private String id;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 认证类型
     * P-密码
     * E-邮箱
     * W-微信
     * Q-QQ
     * B-微博
     */
    private String authType;

    /**
     * 唯一标识(用户名，
     * 邮箱或第三方应用
     * 的唯一标识)
     */
    private String identifier;


    public Authentication toAuthentication() {
        Authentication authentication = new Authentication();
        BeanFieldCopyUtil.copyProperties(this, authentication);
        if (StringUtils.isNotBlank(id)) {
            authentication.setId(Long.parseLong(id));
        }
        return authentication;
    }


}
