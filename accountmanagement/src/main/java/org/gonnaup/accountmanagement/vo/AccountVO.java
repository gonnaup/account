package org.gonnaup.accountmanagement.vo;

import lombok.Data;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.enums.AccountState;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账户VO
 *
 * @author gonnaup
 * @version 2021/1/5 14:14
 */
@Data
public class AccountVO implements Serializable {

    private static final long serialVersionUID = 4236530407141543888L;

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
     * 最近成功登陆时间，可用于清除僵尸账户
     */
    private LocalDateTime lastLogintime;

    /**
     * 账户标记，管理员使用
     */
    private String tag;

    /**
     * 创建时间
     */
    private LocalDate createtime;

    /**
     * 更新时间
     */
    private LocalDate updatetime;

    /**
     * 对象装换
     *
     * @param account
     * @return
     */
    public static AccountVO fromAccount(Account account) {
        AccountVO accountVO = new AccountVO();
        accountVO.setId(Long.toString(account.getId()));
        accountVO.setApplicationName(account.getApplicationName());
        accountVO.setAccountName(account.getAccountName());
        accountVO.setAccountNickname(account.getAccountNickname());
        accountVO.setAccountAvatar(account.getAccountAvatar());
        accountVO.setAccountState(AccountState.valueOf(account.getAccountState()).description());
        accountVO.setLastLogintime(account.getLastLogintime());
        accountVO.setTag(account.getTag());
        accountVO.setCreatetime(LocalDate.from(account.getCreatetime()));
        accountVO.setUpdatetime(LocalDate.from(account.getUpdatetime()));
        return accountVO;
    }

}
