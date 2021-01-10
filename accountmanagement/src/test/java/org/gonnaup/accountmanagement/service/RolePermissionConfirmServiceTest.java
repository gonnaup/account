package org.gonnaup.accountmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.gonnaup.account.domain.Account;
import org.gonnaup.account.domain.Role;
import org.gonnaup.accountmanagement.entity.AccountRole;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.gonnaup.accountmanagement.constant.TestOperaters.ADMIN;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 角色或权限的确认服务测试类
 * @author gonnaup
 * @version 2021/1/8 21:58
 */
@Slf4j
@SpringBootTest
class RolePermissionConfirmServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Autowired
    AccountRoleService accountRoleService;

    @Autowired
    RolePermissionConfirmService rolePermissionConfirmService;

    @Test
    @Transactional
    void rolePermissionConfirmServiceTest() {
        assertTrue(rolePermissionConfirmService.isAdmin(102020121100000001L));
        assertTrue(rolePermissionConfirmService.isAppAdmin(102020121100000001L));
        assertTrue(rolePermissionConfirmService.hasPermission(102020121100000001L, Arrays.stream(RoleType.values()).mapToInt(RoleType::score).toArray()));
        assertTrue(rolePermissionConfirmService.hasPermission(102020121100000001L));

        //创建角色
        Role role1 = new Role();
        role1.setApplicationName("AccountManagement");
        role1.setRoleName("WRITE");
        role1.setScore("0000F000");
        role1.setDescription("写权限");
        Long roleId1 = roleService.insert(role1, ADMIN).getId();

        Account account = new Account();
        account.setAccountAvatar(null);
        account.setAccountName(RandomStringUtils.randomAlphanumeric(5, 20));
        account.setAccountNickname("nickname");
        account.setAccountState("N");
        account.setApplicationName("AccountManagement");
        account.setCreatetime(LocalDateTime.now());
        account.setUpdatetime(LocalDateTime.now());
        Long ID = accountService.insert(account).getId();

        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(ID);
        accountRole.setRoleId(roleId1);
        accountRoleService.insertBatch(List.of(accountRole), ADMIN);

        assertTrue(rolePermissionConfirmService.hasPermission(ID, PermissionType.APP_R.weight()));
    }

}