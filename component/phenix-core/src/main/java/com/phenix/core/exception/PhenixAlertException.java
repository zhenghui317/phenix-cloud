package com.phenix.core.exception;

/**
 * 提示消息异常
 *
 * @author admin
 */
public class PhenixAlertException extends PhenixException {
    private static final long serialVersionUID = 4908906410210213271L;

    public PhenixAlertException() {
    }

    public PhenixAlertException(String msg) {
        super(msg);
    }

    public PhenixAlertException(int code, String msg) {
        super(code, msg);
    }

    public PhenixAlertException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
