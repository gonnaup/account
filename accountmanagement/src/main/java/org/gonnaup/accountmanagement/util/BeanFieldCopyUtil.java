package org.gonnaup.accountmanagement.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用 {@link BeanCopier} 进行对象属性copy
 * @author gonnaup
 * @version 2021/1/15 21:44
 */
public class BeanFieldCopyUtil {

    private static final Map<String, BeanCopier> beanCopierCache = new ConcurrentHashMap<>();

    public static void copyProperties(Object source, Object editable ) {
        Objects.requireNonNull(source, "source object can't be null");
        Objects.requireNonNull(editable, "source object can't be null");

        Class<?> sourceClass = source.getClass();
        Class<?> editableClass = editable.getClass();
        String key = key(sourceClass, editableClass);
        beanCopierCache.putIfAbsent(key, BeanCopier.create(sourceClass, editableClass, false));
        BeanCopier beanCopier = beanCopierCache.get(key);
        beanCopier.copy(source, editable, null);
    }

    private static String key(Class<?> sourceClass, Class<?> editableClass) {
        return sourceClass.getName() + '-' + editableClass.getName();
    }
}
