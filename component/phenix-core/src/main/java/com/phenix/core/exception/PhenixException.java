package com.phenix.core.exception;

import com.phenix.defines.constants.ResponseCode;

/**
 * 基础错误异常
 *
 * @author admin
 */
public class PhenixException extends RuntimeException {

    private static final long serialVersionUID = 3655050728585279326L;

    private int code = ResponseCode.ERROR.getCode();

    public PhenixException() {

    }

    public PhenixException(String msg) {
        super(msg);
    }

    public PhenixException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public PhenixException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
