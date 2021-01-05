package org.gonnaup.accountmanagement.constant;

/**
 * 认证相关常量
 *
 * @author gonnaup
 * @version 2020/12/11 21:56
 */
public class AuthenticateConst {

    private AuthenticateConst() {
    }

    //加密密钥
    public static final String SECRET_KEY = "$8ECA$99";


    //盐
    public static final String SALT = "$ACCOUNT$";

    //jwt http header名
    public static final String JWT_HEADER_NAME = "token_jwt";

    //缓存在HttpServletRequest应用名属性中的属性名
    public static final String REQUEST_ATTR_JWTDATA = "JWTDATA";

    //jwt过期时间 7天
    public static final Long JWT_EXPIRE_TIME = 7 * 24 * 60 * 60L;

    //手动失效的jwt在redis中的key前缀值
    public static final String JWT_BLACKLIST_REDIS_PREFIX = ApplicationName.APPNAME + "::jwt_blacklist::";

}
