package org.gonnaup.account.exception;

/**
 * 登录认证异常
 *
 * @author gonnaup
 * @version 2020/12/11 21:50
 */
public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = -2620035268635469452L;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

}
