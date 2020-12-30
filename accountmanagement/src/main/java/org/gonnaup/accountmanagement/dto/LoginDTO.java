package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 登录dto
 * @author gonnaup
 * @version 2020/12/11 11:58
 */
@Data
@Validated
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 4501723482498124405L;
    /**
     * 用户名
     */
    @NotNull(message = "用户名/邮箱不能为空")
    private String identifier;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 6, message = "密码需大于6位")
    private char[] credential;

}
