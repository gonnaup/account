package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Account;
import org.gonnaup.accountmanagement.validator.ApplicationNameAccessor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 账户查询DTO
 * @author gonnaup
 * @version 2021/1/1 15:23
 */
@Data
public class AccountQueryDTO implements Serializable, ApplicationNameAccessor {

    private static final long serialVersionUID = -1495379259264226616L;
    /**
     * ID
     */
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
        BeanUtils.copyProperties(this, account);
        account.setId(Long.parseLong(getId()));
        return account;
    }


}
