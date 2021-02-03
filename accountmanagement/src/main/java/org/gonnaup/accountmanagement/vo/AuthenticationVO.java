package org.gonnaup.accountmanagement.vo;

import lombok.Data;
import org.gonnaup.account.domain.Authentication;
import org.gonnaup.account.enums.AuthType;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * {@link org.gonnaup.account.domain.Authentication} VO
 * @author gonnaup
 * @version 2021/1/17 21:03
 */
@Data
public class AuthenticationVO implements Serializable {

    private static final long serialVersionUID = 4410019482126456211L;
    /**
     * ID
     */
    private String id;

    /**
     * 账户ID
     */
    private String accountId;

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

    /**
     * 凭证(密码或第三方token)
     */
    private String credential;

    /**
     * 过期时间，OAuth2登录时使用
     */
    private String expires;


    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 更新时间
     */
    private LocalDateTime updatetime;

    public static AuthenticationVO fromAuthentication(Authentication authentication) {
        AuthenticationVO authenticationVO = new AuthenticationVO();
        BeanFieldCopyUtil.copyProperties(authentication, authenticationVO);
        authenticationVO.setId(Long.toString(authentication.getId()));
        authenticationVO.setAccountId(Long.toString(authentication.getAccountId()));
        authenticationVO.setAuthType(AuthType.valueOf(authentication.getAuthType()).description());
        if (authentication.getExpires() != null) {
            authenticationVO.setExpires(Long.toString(authentication.getExpires()));
        }
        return authenticationVO;
    }

}
