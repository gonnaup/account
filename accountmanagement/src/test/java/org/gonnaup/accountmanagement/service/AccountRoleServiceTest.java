package org.gonnaup.accountmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.gonnaup.accountmanagement.constant.TestOperaters.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link AccountRoleService} 测试类
 *
 * @author gonnaup
 * @version 2020/12/21 20:03
 */
@Slf4j
@SpringBootTest
class AccountRoleServiceTest {

    @Autowired
    AccountRoleService accountRoleService;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Test
    @Transactional
    @Rollback
    void accountTest() {
        Account account = new Account();
        account.setAccountAvatar(null);
        account.setAccountName("jtest_name");
        account.setAccountNickname("nickname");
        account.setAccountState("N");
        account.setApplicationName("AccountManagement");
        account.setCreatetime(LocalDateTime.now());
        account.setUpdatetime(LocalDateTime.now());
        Long ID = accountService.insert(account).getId();
        accountService.findById(ID);//cache test
        Assertions.assertNotNull(accountService.findById(ID));
        Assertions.assertNotNull(accountService.findHeaderById(ID));
        accountService.deleteById(ID);
    }


    @Test
    @Transactional
    void accountRoleTest() {
        // 创建账号
        Account account = new Account();
        account.setAccountAvatar(null);
        account.setAccountName(RandomStringUtils.randomAlphanumeric(5, 20));
        account.setAccountNickname("nickname");
        account.setAccountState("N");
        account.setApplicationName("AccountManagement");
        account.setCreatetime(LocalDateTime.now());
        account.setUpdatetime(LocalDateTime.now());
        Long accountId = accountService.insert(account).getId();

        //创建角色
        Role role1 = new Role();
        role1.setApplicationName("AccountManagement");
        role1.setRoleName("admin");
        role1.setDescription("");
        Long roleId1 = roleService.insert(role1, ADMIN).getId();

        Role role2 = new Role();
        role2.setApplicationName("AccountManagement");
        role2.setRoleName("app");
        role2.setDescription("");
        Long roleId2 = roleService.insert(role2, ADMIN).getId();

        //创建账号权限
        AccountRole accountRole1 = new AccountRole();
        accountRole1.setAccountId(accountId);
        accountRole1.setRoleId(roleId1);

        AccountRole accountRole2 = new AccountRole();
        accountRole2.setAccountId(accountId);
        accountRole2.setRoleId(roleId2);

        //插入数据
        List<AccountRole> accountRoleList = List.of(accountRole1, accountRole2);
        accountRoleService.insertBatch(accountRoleList, ADMIN);

        //测试查询方法
        assertEquals(accountRoleService.findRolesByAccountId(accountId).size(), 2);

        //删除一个
        accountRoleService.deleteMany(accountId, List.of(roleId1), "AccountManagement", ADMIN);
        assertEquals(accountRoleService.findRolesByAccountId(accountId).size(), 1);

        //删除所有
        accountRoleService.deleteByAccountId(accountId, "AccountManagement", ADMIN);
        assertEquals(accountRoleService.findRolesByAccountId(accountId).size(), 0);

    }


}