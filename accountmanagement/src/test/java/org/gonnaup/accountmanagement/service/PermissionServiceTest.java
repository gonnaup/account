package org.gonnaup.accountmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.Permission;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.constant.TestOperaters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限服务测试类
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/12 16:21
 */
@SpringBootTest
@ActiveProfiles({"default", "postgresql"})
@Slf4j
class PermissionServiceTest {

    @Autowired
    PermissionService permissionService;

    private static final String pmName = "ALL_PERMISSION";

    private static final Permission permission = new Permission();

    static {
        permission.setApplicationName(ApplicationName.APPNAME);
        permission.setPermissionName(pmName);
        permission.setDescription("it is the root");
    }

    @Test
    @Transactional
    @Rollback
    void testAllMethod() {
        log.info("====================插入和查询测试===================");
        Permission permissionInserted = permissionService.insert(PermissionServiceTest.permission, TestOperaters.ADMIN);
        Assertions.assertNotNull(permissionService.findById(permissionInserted.getId()));

        log.info("===================更新测试=======================");
        permissionInserted.setPermissionName("ALL");
        permissionService.update(permissionInserted, TestOperaters.ADMIN);
        Assertions.assertEquals(permissionService.findById(permissionInserted.getId()).getPermissionName(), pmName);

        log.info("==================删除测试========================");
        permissionService.deleteById(permissionInserted.getId(), TestOperaters.ADMIN);
        Assertions.assertNull(permissionService.findById(permissionInserted.getId()));

    }

}