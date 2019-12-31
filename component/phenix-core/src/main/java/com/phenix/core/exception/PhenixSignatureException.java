package com.phenix.core.exception;

/**
 * 签名异常
 *
 * @author admin
 */
public class PhenixSignatureException extends PhenixException {
    private static final long serialVersionUID = 4908906410210213271L;

    public PhenixSignatureException() {
    }

    public PhenixSignatureException(String msg) {
        super(msg);
    }

    public PhenixSignatureException(int code, String msg) {
        super(code, msg);
    }

    public PhenixSignatureException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
