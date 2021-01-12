package org.gonnaup.account.exception;

/**
 * 逻辑验证异常
 *
 * @author gonnaup
 * @version 2021/1/12 10:11
 */
public class LogicValidationException extends RuntimeException {

    private static final long serialVersionUID = 2873378895122630232L;

    public LogicValidationException() {
        super();
    }

    public LogicValidationException(String message) {
        super(message);
    }
}
