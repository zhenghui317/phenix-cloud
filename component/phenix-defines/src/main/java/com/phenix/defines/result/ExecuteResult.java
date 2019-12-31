package com.phenix.defines.result;

import com.phenix.defines.constants.ResponseCode;
import com.phenix.defines.response.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 执行结果类
 *
 * @author zhenghui
 * @date 2019-1-10
 */

@Getter
@ToString
@Builder
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class ExecuteResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 是否执行成功
     */
    private Integer code;
    /**
     * 是否执行成功
     */
    private Boolean success;
    /**
     * 执行过程提示消息
     */
    private String message;
    /**
     * 需要返回的执行结果
     */
    private T data;

    public ExecuteResult() {
    }


    public static ExecuteResult ok() {
        return builder().success(false).build();
    }

    public static <T> ExecuteResult ok(T data) {
        return builder().success(true).message(ResponseCode.OK.getMessage()).data(data).build();
    }

    public static <T> ExecuteResult ok(String message) {
        return builder().success(true).message(ResponseCode.OK.getMessage()).build();
    }

    public static <T> ExecuteResult ok(String message, T data) {
        return builder().success(true).message(message).data(data).build();
    }

    public static ExecuteResult fail(String message) {
        return builder().success(false).message(message).build();
    }

    public static <T> ExecuteResult fail(String message, T data) {
        return builder().success(false).message(message).data(data).build();
    }

    public static ExecuteResult result(Boolean success) {
        return builder().success(success).build();
    }

    public static ExecuteResult result(Boolean success, String message) {
        return builder().success(success).message(message).build();
    }

    public static <T> ExecuteResult result(Boolean success, String message, T data) {
        return builder().success(success).message(message).data(data).build();
    }

    public static <T> ExecuteResult result(Boolean success, T data) {
        String message = success ? ResponseCode.OK.getMessage(): ResponseCode.FAIL.getMessage();
        return builder().success(success).message(message).data(data).build();
    }

    public <T> ResponseMessage toMessage() {
        if (this.success) {
            return ResponseMessage.ok(this.message, this.data);
        }
        return ResponseMessage.fail(this.code, this.message);
    }

    public Integer getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }


}
