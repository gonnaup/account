package org.gonnaup.accountmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.ApplicationSequenceKey;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.constant.TestOperaters;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.entity.ApplicationSequence;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.common.domain.Page;
import org.gonnaup.common.domain.Pageable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * 应用序列服务测试
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/11 14:19
 */
@SpringBootTest
@Slf4j
class ApplicationSequenceServiceTest {
    @Autowired
    ApplicationSequenceService applicationSequenceService;

    private static final ApplicationSequenceKey TEST_KEY = ApplicationSequenceKey.of(ApplicationName.APPNAME, "TEST_DSGADXXXSDFFF");

    @Test
    void produceSequence() {
        applicationSequenceService.insert(ApplicationSequence.of(TEST_KEY, 99999859, 100), TestOperaters.ADMIN);
        Set<Long> seq = Collections.newSetFromMap(new ConcurrentHashMap<>());
        Set<Integer> index = Collections.newSetFromMap(new ConcurrentHashMap<>());
        IntStream.range(1, 1000).parallel().forEach(i -> {
            long sequence = applicationSequenceService.produceSequence(TEST_KEY);
            seq.add(sequence);
            index.add(i);
            log.info("{} 获取sequence {}", i, sequence);
        });
        Assertions.assertEquals(seq.size(), index.size());
        log.info("seq总共 {} 个", seq.size());
        log.info("index总共 {} 个", index.size());
        applicationSequenceService.deleteOne(TEST_KEY, TestOperaters.ADMIN);
    }

    @Test
    @Transactional
    @Rollback
    void findAllConditionalPaged() {
        applicationSequenceService.insert(ApplicationSequence.of(TEST_KEY, 0, 100), Operater.of(OperaterType.A, 1000000000L, "admin"));
        Page<ApplicationSequence> paged = applicationSequenceService.findAllConditionalPaged(TEST_KEY, Pageable.of(1, 1));
        Assertions.assertEquals(paged.getTotal(), 1);
        applicationSequenceService.deleteOne(TEST_KEY, TestOperaters.ADMIN);
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        applicationSequenceService.insert(ApplicationSequence.of(TEST_KEY, 0, 100), Operater.of(OperaterType.A, 1000000000L, "admin"));
        applicationSequenceService.update(ApplicationSequence.of(TEST_KEY, 1000, 200), TestOperaters.ADMIN);
        Page<ApplicationSequence> paged = applicationSequenceService.findAllConditionalPaged(TEST_KEY, Pageable.of(1, 1));
        Assertions.assertEquals(paged.getData().get(0).getStep(), 200);
        Assertions.assertEquals(paged.getData().get(0).getSequence(), 0);
        applicationSequenceService.deleteOne(TEST_KEY, TestOperaters.ADMIN);
    }

    @Test
    void delete() {
        applicationSequenceService.insert(ApplicationSequence.of(TEST_KEY, 0, 100), Operater.of(OperaterType.A, 1000000000L, "admin"));
        applicationSequenceService.deleteOne(TEST_KEY, TestOperaters.ADMIN);
        Page<ApplicationSequence> paged = applicationSequenceService.findAllConditionalPaged(TEST_KEY, Pageable.of(1, 1));
        Assertions.assertEquals(paged.getTotal(), 0);
    }

}