package org.gonnaup.accountmanagement.constant;

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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author gonnaup
 * @version 2020/12/27 20:33
 */
@Slf4j
public class JsonUtil {

    public static final ObjectMapper objectMapper;

    static {
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
        objectMapper = new ObjectMapper()
                .registerModule(dateModule)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);//必须设置，否则返回LinkedHashMap类型导致ClassCastException
    }

}
