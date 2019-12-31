package com.phenix.admin.pojo.parameter;

import com.phenix.starter.web.common.serializer.CustomLongDeserializer;
import com.phenix.starter.web.common.serializer.CustomLongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

/**
 * 动作参数
 * @author zhenghui
 * @date 2019-12-20
 */
@Getter
public class AuthorityActionParameter extends AuthorityParameter {

    /**
     * 动作id
     */
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonSerialize(using = CustomLongSerializer.class)
    private Long actionId;
}
