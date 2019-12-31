package com.phenix.tools.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Long类型反序列化
 *
 * @author zhenghui
 * @date 2018-11-09
 */
public class CustomLongDeserializer extends JsonDeserializer<Long> {
    private static final Logger logger = LoggerFactory.getLogger(CustomLongDeserializer.class);

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        try {
            return StringUtils.isBlank(value) ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.error("解析长整形错误", e);
            return null;
        }
    }
}
