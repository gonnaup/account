package org.gonnaup.account.exception;

/**
 * 登录异常类
 * @author gonnaup
 * @version 2020/12/28 12:09
 */
public class LoginException extends Exception {

    private static final long serialVersionUID = 5548867106533966025L;

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

}
