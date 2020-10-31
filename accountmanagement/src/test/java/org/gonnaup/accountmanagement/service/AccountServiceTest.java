package org.gonnaup.accountmanagement.service;

import org.gonnaup.account.domain.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/10/29 21:21
 */
@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    @Transactional
    @Rollback
    void queryById() {

    }

    @Test
    void queryAllByLimit() {
        accountService.queryAllByLimit(1, 10);
    }

    @Test
    void insert() {
        Account account = new Account();
        account.setId(100989489384L);
        account.setAccountAvatar(null);
        account.setAccountName("jtest_name");
        account.setAccountNickname("nickname");
        account.setAccountStatu("N");
        account.setApplicationName("AccountManagement");
        account.setCreatetime(LocalDateTime.now());
        account.setUpdatetime(LocalDateTime.now());
        accountService.insert(account);
        Assertions.assertNotNull(accountService.queryById(100989489384L));
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}