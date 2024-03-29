package org.gonnaup.accountmanagement;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.enums.OperaterType;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.gonnaup.common.util.CryptUtil;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
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

    public static void main(String[] args) {
        System.out.println(CryptUtil.md5Encode("admin", AuthenticateConst.SALT));
        System.out.println(0xffff);
        /**
         * 模拟权限
         */
        System.out.println((PermissionType.ALL.weight() & PermissionType.APP_A.weight()) == PermissionType.APP_A.weight());
        System.out.println(StringUtils.leftPad(Integer.toHexString(RoleType.APPRDAU.score()).toUpperCase(), 8, '0'));
        System.out.println(Integer.valueOf("0000F000", 16));
    }

}
