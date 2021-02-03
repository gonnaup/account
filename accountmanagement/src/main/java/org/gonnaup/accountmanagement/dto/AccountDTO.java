package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.Authentication;
import org.gonnaup.account.enums.AuthType;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.gonnaup.common.util.CryptUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author gonnaup
 * @version 2021/1/4 22:15
 */
@Data
@Validated
public class AccountDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = 8472465126440580678L;

    //***************账号信息******************//
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 用户名，同一应用中唯一
     */
    private String accountName;

    /**
     * 昵称
     */
    @NotNull(message = "昵称不能为空")
    private String accountNickname;

    /**
     * 头像
     */
    private String accountAvatar;

    /**
     * 账户状态
     * N - 正常
     * F - 禁用
     */
    private String accountState;

    /**
     * 账户标记，管理员使用
     */
    private String tag;

    //****************认证信息***************//
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    private String identifier;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 6, message = "密码需大于6位")
    private char[] credential;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;


    /**
     * 对象转换
     *
     * @return
     */
    public Account toAccount() {
        Account account = new Account();
        BeanUtils.copyProperties(this, account);
        return account;
    }

    /**
     * 为账号生成认证信息
     *
     * @param accountId
     * @return
     */
    public Authentication createAuthentication(Long accountId) {
        Authentication authentication = new Authentication();
        authentication.setAccountId(accountId);
        authentication.setApplicationName(applicationName);
        authentication.setAuthType(AuthType.E.name());//email类型
        authentication.setIdentifier(identifier);
        authentication.setCredential(CryptUtil.md5Encode(new String(credential), AuthenticateConst.SALT));
        authentication.setExpires(-1L);
        credential = null;//help gc
        return authentication;
    }

}
