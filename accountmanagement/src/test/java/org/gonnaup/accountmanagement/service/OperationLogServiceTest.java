package org.gonnaup.accountmanagement.service;

import org.gonnaup.accountmanagement.constant.TestOperaters;
import org.gonnaup.accountmanagement.entity.OperationLog;
import org.gonnaup.accountmanagement.enums.OperateType;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作日志服务测试类
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/11 11:35
 */
@SpringBootTest
@ActiveProfiles({"default", "postgresql"})
class OperationLogServiceTest {

    @Autowired
    OperationLogService operationLogService;

    @Test
    @Transactional
    @Rollback
    void insertAndFindByIdAndDelete() {
        OperationLog test = OperationLog.of(TestOperaters.ADMIN, OperateType.A, "test");
        OperationLog inserted = operationLogService.insert(test);
        Assertions.assertNotNull(operationLogService.findById(inserted.getId()));

        inserted.setOperateDetail("test-update");
        inserted.setOperateType(OperateType.D.name());
        operationLogService.update(inserted);
        OperationLog updated = operationLogService.findById(inserted.getId());
        Assertions.assertEquals(updated.getOperateDetail(), "test-update");
        Assertions.assertEquals(updated.getOperateType(), OperateType.D.name());

        operationLogService.deleteById(inserted.getId());
        Assertions.assertNull(operationLogService.findById(inserted.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void findAllConditionalByLimit() {
        OperationLog test = OperationLog.of(TestOperaters.ADMIN, OperateType.A, "test");
        OperationLog inserted = operationLogService.insert(test);
        OperationLog query = new OperationLog();
        query.setOperaterName(TestOperaters.ADMIN.getOperaterName());
        Page<OperationLog> paged = operationLogService.findAllConditionalPaged(query, Pageable.of(1, 1));
        Assertions.assertEquals(paged.getData().size(), 1);
        operationLogService.deleteById(inserted.getId());
    }

}