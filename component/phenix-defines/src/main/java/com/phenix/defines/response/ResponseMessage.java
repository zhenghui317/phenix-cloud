package com.phenix.defines.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.phenix.defines.constants.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.ResourceBundle;


/**
 * 控制器返回数据类型
 *
 * @author zhenghui
 * @date 2018/3/28
 */
@Getter
@ToString
@Builder
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class ResponseMessage<T> implements Serializable {

    private static final long serialVersionUID = -8469897193657150350L;

    /**
     * 成功编码
     */
    public static final Integer SUCCESS_CODE = 0;

    /**
     * 错误编码
     */
    public static final Integer ERROR_CODE = -2;

    /**
     * 成功消息
     */
    public static final String SUCCESS_MESSAGE = "success";
    /**
     * 错误消息
     */
    public static final String ERROR_MESSAGE = "error";
    /**
     * 是否操作成标识
     */
    private boolean success = false;
    /**
     * 编码,当返回有多种可能的情况下使用编码区分结果状态
     */
    private Integer code = 0;

    /**
     * 提示信息
     */
    private String message = "";

    /**
     * 返回的Http状态
     */
    private Integer httpStatus;

    /**
     * 返回数据
     */
    private T data = null;

    public ResponseMessage() {
        super();
        this.success = true;
    }


    public static ResponseMessage ok() {
        return builder().success(true).code(ResponseCode.OK.getCode()).message(ResponseCode.OK.getMessage()).build();
    }

    public static <T> ResponseMessage ok(T data) {
        return builder().success(true).code(ResponseCode.OK.getCode()).message(ResponseCode.OK.getMessage()).data(data).build();
    }

    public static <T> ResponseMessage ok(String message, T data) {
        return builder().success(true).code(ResponseCode.OK.getCode()).message(message).data(data).build();
    }

    public static ResponseMessage fail() {
        return builder().success(false).code(ResponseCode.FAIL.getCode()).message(ResponseCode.FAIL.getMessage()).build();
    }

    public static ResponseMessage fail(String errMessage) {
        return builder().success(false).code(ResponseCode.FAIL.getCode()).message(errMessage).build();
    }

    public static ResponseMessage fail(Integer code, String errMessage) {
        return builder().success(false).code(code).message(errMessage).build();
    }

    public static ResponseMessage result() {
        return new ResponseMessage();
    }

    public static ResponseMessage result(boolean success) {
        return builder().success(success).build();
    }
//
//    /**
//     * 错误信息配置
//     */
//    @JSONField(serialize = false, deserialize = false)
//    @JsonIgnore
//    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("error");
//
//    /**
//     * 提示信息国际化
//     *
//     * @param message
//     * @param defaultMessage
//     * @return
//     */
//    @JSONField(serialize = false, deserialize = false)
//    @JsonIgnore
//    private static String i18n(String message, String defaultMessage) {
//        return resourceBundle.containsKey(message) ? resourceBundle.getString(message) : defaultMessage;
//    }

}

