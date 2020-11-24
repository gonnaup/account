package org.gonnaup.accountmanagement;

import com.github.javafaker.Faker;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 普通java代码测试
 *
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/13 20:58
 */
@Testable
@Slf4j
public class AppTest {


    @Test
    void testCollect() {
        List<Operater> operaters = List.of(
                Operater.of(OperaterType.A, 1000003232L, "a1"),
                Operater.of(OperaterType.A, 1000003232L, "a2"),
                Operater.of(OperaterType.A, 1000003232L, "a31"),
                Operater.of(OperaterType.A, 1000003232L, "a14"),
                Operater.of(OperaterType.S, 1000003232L, "a15"),
                Operater.of(OperaterType.S, 1000003232L, "a16"),
                Operater.of(OperaterType.S, 1000003232L, "a17"),
                Operater.of(OperaterType.S, 1000003232L, "a18"),
                Operater.of(OperaterType.S, 1000003232L, "a19"),
                Operater.of(OperaterType.S, 1000003232L, "a167"),
                Operater.of(OperaterType.S, 1000003232L, "a155")
        );

        Map<OperaterType, List<String>> collect = operaters.stream().collect(Collectors.groupingBy(Operater::getOperaterType,
                Collector.of((Supplier<List<String>>) ArrayList::new, (list, o) -> list.add(Strings.padEnd(o.getOperaterName().toUpperCase(), 8, '0')),
                        (list, list2) -> {
                            list.addAll(list2);
                            return list;
                        })));
        log.info("收集的map => {}", collect);
    }

    @Test
    void stringJoiner() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        joiner.add("A").add("B").add("C");
        log.info("joiner {}", joiner);

        StringJoiner empty = new StringJoiner(",", "[", "]");
        log.info("empty joiner {}", empty);

        StringJoiner emptyWithValue = new StringJoiner(",", "[", "]");
        emptyWithValue.setEmptyValue("");
        log.info("empty joiner {}", emptyWithValue);
    }

    @Test
    void javafaker() {
        Map<String, String> map = new HashMap<>();
        Faker faker = new Faker(Locale.CHINA);
        map.put("name", faker.name().fullName());
        map.put("company", faker.company().name());
        map.put("weather", faker.weather().description());
        log.info(map.toString());
    }

}