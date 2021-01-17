package org.gonnaup.accountmanagement.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.time.Duration;
import java.util.Objects;

/**
 * 使用 {@link BeanCopier} 进行对象属性copy
 *
 * @author gonnaup
 * @version 2021/1/15 21:44
 */
@Slf4j
public class BeanFieldCopyUtil {

    /**
     * {@link BeanCopier} 缓存
     */
    private static final LoadingCache<CacheKeyWapper, BeanCopier> beanCopierCache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMillis(10))
            .build(new CacheLoader<CacheKeyWapper, BeanCopier>() {
                @Override
                public BeanCopier load(CacheKeyWapper cacheKeyWapper) {
                    log.info("加载BeanCopier，key = {}", cacheKeyWapper);
                    return BeanCopier.create(cacheKeyWapper.source, cacheKeyWapper.editable, false);
                }
            });

    /**
     * 从<code>source</code>对象中复制属性到<code>editable</code>对象
     * @param source
     * @param editable
     */
    public static void copyProperties(Object source, Object editable) {
        Objects.requireNonNull(source, "source object can't be null");
        Objects.requireNonNull(editable, "source object can't be null");

        Class<?> sourceClass = source.getClass();
        Class<?> editableClass = editable.getClass();
        BeanCopier beanCopier = beanCopierCache.getUnchecked(new CacheKeyWapper(sourceClass, editableClass));
        beanCopier.copy(source, editable, null);
    }

    /**
     * BeanCopier 缓存key对象
     */
    @ToString
    @EqualsAndHashCode
    private static class CacheKeyWapper {
        private final Class<?> source;
        private final Class<?> editable;

        public CacheKeyWapper(Class<?> source, Class<?> editable) {
            this.source = source;
            this.editable = editable;
        }
    }
}
