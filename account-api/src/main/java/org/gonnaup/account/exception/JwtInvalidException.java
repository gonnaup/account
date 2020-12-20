package org.gonnaup.account.exception;

/**
 * jwt无效异常
 *
 * @author gonnaup
 * @version 2020/12/20 20:27
 */
public class JwtInvalidException extends Exception {

    private static final long serialVersionUID = -8969604977980132842L;

    public JwtInvalidException() {
        super();
    }

    public JwtInvalidException(String message) {
        super(message);
    }

}
