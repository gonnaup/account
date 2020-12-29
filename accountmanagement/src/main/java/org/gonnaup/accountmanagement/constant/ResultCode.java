package org.gonnaup.accountmanagement.constant;

/**
 * 返回码
 * @author gonnaup
 * @version 2020/12/28 12:13
 */
public enum ResultCode {

    SUCCESS("200"),//成功
    FAIL("400"),//失败
    NOTLOGIN_ERROR("401"),//未登录
    AUTH_ERROR("402"),//鉴权失败
    LOGIN_ERROR("403"),//登录失败
    SYSTEM_ERROR("500");//服务器异常

    private final String code;

    private ResultCode(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

}
