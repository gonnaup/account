package org.gonnaup.accountmanagement.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Object工具类
 * @author gonnaup
 * @version 2021/2/3 20:26
 */
@Slf4j
public class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * 如果对象为null则抛出异常
     * @param object 对象
     * @param exception 抛出的异常
     * @return 对象本身
     */
    public static <T> T requireNotNullThrows(T object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        }
        return object;
    }

    /**
     * 如果对象为null则抛出异常
     * @param object 对象
     * @param exception 抛出的异常
     * @param errorLog 需要记录的错误日志
     * @return 对象本身
     */
    public static <T> T requireNotNullThrows(T object, RuntimeException exception, String errorLog) {
        if (object == null) {
            log.error(errorLog);
            throw exception;
        }
        return object;
    }

}
