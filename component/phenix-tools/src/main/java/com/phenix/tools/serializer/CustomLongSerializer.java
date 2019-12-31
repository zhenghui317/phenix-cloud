package com.phenix.tools.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *  Long类型序列化
 *  @author  zhenghui
 *  @date 2018-11-09
 *  JS在使用Long型时会有精度损失，因此需要转换成String型
 */
public class CustomLongSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        String text = (value == null ? null : String.valueOf(value));
        if (text != null) {
            jsonGenerator.writeString(text);
        }
    }
}
