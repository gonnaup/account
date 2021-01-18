package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.accountmanagement.util.BeanFieldCopyUtil;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 账户查询DTO
 * @author gonnaup
 * @version 2021/1/1 15:23
 */
@Data
@Valid
public class AccountQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = -1495379259264226616L;
    /**
     * ID
     */
    @Pattern(regexp = "^[0-9]*$", message = "ID必须为数字")
    private String id;

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
    private String accountNickname;

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

    /**
     * 对象转换
     * @return {@link Account}
     */
    public Account toAccount() {
        Account account = new Account();
        BeanFieldCopyUtil.copyProperties(this, account);
        if (StringUtils.isNotBlank(id)) {
            account.setId(Long.parseLong(id));
        }
        return account;
    }


}
