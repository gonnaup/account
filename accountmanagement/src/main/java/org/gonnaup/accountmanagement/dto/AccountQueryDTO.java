package org.gonnaup.accountmanagement.dto;

import lombok.Data;
import org.gonnaup.account.domain.Account;

/**
 * 账户查询DTO
 * @author gonnaup
 * @version 2021/1/1 15:23
 */
@Data
public class AccountQueryDTO {

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
        account.setId(Long.parseLong(getId()));
        account.setApplicationName(getApplicationName());
        account.setAccountName(getAccountName());
        account.setAccountNickname(getAccountNickname());
        account.setAccountState(getAccountState());
        account.setTag(getTag());
        return account;
    }


}
