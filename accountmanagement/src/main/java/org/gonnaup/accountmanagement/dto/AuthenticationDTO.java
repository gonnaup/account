package org.gonnaup.accountmanagement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Authentication;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;
import org.gonnaup.common.util.CryptUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.StringJoiner;

/**
 * 认证信息 dto
 *
 * @author gonnaup
 * @version 2021/1/19 13:11
 */
@Getter
@Setter
@EqualsAndHashCode
@Valid
public class AuthenticationDTO implements Serializable {

    private static final long serialVersionUID = 866313490719836156L;
    /**
     * ID
     */
    @Pattern(regexp = "^[0-9]*$", message = "ID必须为数字")
    private String id;

    /**
     * 唯一标识(用户名，
     * 邮箱或第三方应用
     * 的唯一标识)
     */
    @NotNull(message = "用户名/邮箱不能为空")
    private String identifier;

    /**
     * 凭证(密码或第三方token)
     */
    @Size(min = 6, message = "密码需大于6位")
    private char[] credential;

    /**
     * 过期时间，OAuth2登录时使用
     */
    private Long expires;


    public Authentication toAuthentication() {
        Authentication authentication = new Authentication();
        BeanFieldCopyUtil.copyProperties(this, authentication);
        if (StringUtils.isNotBlank(id)) {
            authentication.setId(Long.parseLong(id));
        }
        if (ArrayUtils.isNotEmpty(credential)) {//如果不修改密码，前台不赋值此属性
            authentication.setCredential(CryptUtil.md5Encode(new String(credential), AuthenticateConst.SALT));//加密
        }
        return authentication;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthenticationDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("identifier='" + identifier + "'")
                .add("expires=" + expires)
                .toString();
    }
}
