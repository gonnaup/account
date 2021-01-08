package org.gonnaup.accountmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** 角色或权限的确认服务测试类
 * @author gonnaup
 * @version 2021/1/8 21:58
 */
@Slf4j
@SpringBootTest
class RolePermissionConfirmServiceTest {

    @Autowired
    RolePermissionConfirmService rolePermissionConfirmService;

    @Test
    void rolePermissionConfirmServiceTest() {
        assertTrue(rolePermissionConfirmService.isAdmin(102020121100000001L));
        assertTrue(rolePermissionConfirmService.isAppAdmin(102020121100000001L));
        assertTrue(rolePermissionConfirmService.hasPermission(102020121100000001L, Arrays.stream(RoleType.values()).map(RoleType::score).toArray(Integer[]::new)));
        assertTrue(rolePermissionConfirmService.hasPermission(102020121100000001L));
    }

}