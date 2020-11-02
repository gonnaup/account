package org.gonnaup.account.domain;

import lombok.Data;

import java.io.Serializable;

/** 账户核心信息
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/1 20:08
 */
@Data
public class AccountHeader implements Serializable {

    private static final long serialVersionUID = -5648343139832680637L;

    private Long id;

    private String accountName;

    private String accountNickname;

    private byte[] accountAvatar;

    public Account toAccount() {
        Account account = new Account();
        account.setId(id);
        account.setAccountName(accountName);
        account.setAccountNickname(accountNickname);
        account.setAccountAvatar(accountAvatar);
        return account;
    }

}
