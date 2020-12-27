package org.gonnaup.accountmanagement.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.constant.FormaterConst;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * redis, redisCache config
 *
 * @author gonnaup
 * @version 2020/12/24 12:43
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedisCacheManager.class)
@EnableConfigurationProperties(CacheProperties.class)
public class RedisConfig {

    /**
     * 自定义{@link RedisCacheManager},支持模糊匹配驱逐
     */
    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties) {
        CacheProperties.Redis cachePropertiesRedis = cacheProperties.getRedis();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(cachePropertiesRedis.getKeyPrefix())
                .entryTtl(cachePropertiesRedis.getTimeToLive())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(createJackson2JsonRedisSerializer()));
        return new PatternRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), redisCacheConfiguration);
    }

    /**
     * json序列化器
     *
     * @return
     */
    private RedisSerializer<Object> createJackson2JsonRedisSerializer() {
        SimpleModule dateModule = new SimpleModule();
        dateModule.addSerializer(LocalDate.class, new LocalDateSerializer(FormaterConst.LOCALDATEFORMATTER));
        dateModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(FormaterConst.LOCALDATETIMEFORMATTER));
        dateModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(FormaterConst.DATE_PATTERN)));
        dateModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(FormaterConst.LOCALDATEFORMATTER));
        dateModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(FormaterConst.LOCALDATETIMEFORMATTER));
        dateModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
            private static final long serialVersionUID = -8510993666708640323L;
            final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(FormaterConst.DATE_PATTERN));

            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) {
                String text = null;
                try {
                    text = p.getText().trim();
                    return dateFormat.get().parse(text);
                } catch (ParseException | IOException e) {
                    log.error("jackson反序列化Date[{}]失败", text);
                    return null;
                }
            }
        });
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(dateModule)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);//必须设置，否则返回LinkedHashMap类型导致ClassCastException
        return new GenericJackson2JsonRedisSerializer(mapper);
    }


    /**
     * cache实例为{@link PatternRedisCache}的CacheManager
     */
    public static class PatternRedisCacheManager extends RedisCacheManager {

        private final RedisCacheWriter cacheWriter;

        private final RedisCacheConfiguration defaultCacheConfig;

        public PatternRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
            super(cacheWriter, defaultCacheConfiguration);
            this.cacheWriter = cacheWriter;
            this.defaultCacheConfig = defaultCacheConfiguration;
        }

        public PatternRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
            super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
            this.cacheWriter = cacheWriter;
            this.defaultCacheConfig = defaultCacheConfiguration;
        }

        public PatternRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
            super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
            this.cacheWriter = cacheWriter;
            this.defaultCacheConfig = defaultCacheConfiguration;
        }

        public PatternRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
            super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
            this.cacheWriter = cacheWriter;
            this.defaultCacheConfig = defaultCacheConfiguration;
        }

        public PatternRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
            super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
            this.cacheWriter = cacheWriter;
            this.defaultCacheConfig = defaultCacheConfiguration;
        }

        /**
         * 返回自定义RedisCache类
         *
         * @param name
         * @param cacheConfig
         * @return
         */
        @Override
        public RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
            return new PatternRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
        }
    }


    /**
     * 支持cache模糊删除的redisCache类，只支持后缀模糊化，eg. "cache*"
     */
    public static class PatternRedisCache extends RedisCache {

        private final ConversionService conversionService;

        protected PatternRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
            super(name, cacheWriter, cacheConfig);
            this.conversionService = cacheConfig.getConversionService();
        }

        /**
         * 清除缓存方法，
         * key支持"*"后缀
         *
         * @param key key
         */
        @Override
        public void evict(Object key) {
            if (key instanceof String) {
                String k = (String) key;
                //key以"*"结尾则进行模糊删除
                if (k.endsWith("*")) {
                    byte[] pattern = conversionService.convert(createCacheKey(k), byte[].class);
                    Assert.notNull(pattern, "key pattern must not null");
                    getNativeCache().clean(getName(), pattern);
                }
            }
            super.evict(key);
        }
    }

}
