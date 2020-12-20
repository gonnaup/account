package org.gonnaup.accountmanagement.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.accountmanagement.constant.ApplicationName;

import java.security.Key;
import java.util.Date;

/**
 * jwt生成和验证工具类
 *
 * @author gonnaup
 * @version 2020/12/20 14:53
 */
@Slf4j
public class JWTUtil {

    private JWTUtil() {
    }

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 签发jwt
     *
     * @param sub        账号id
     * @param expireDate 过期时间
     * @return jwt
     */
    public static String signJWT(Long sub, Date expireDate) {
        return Jwts.builder().setSubject(Long.toString(sub))
                .setIssuer(ApplicationName.APPNAME)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 验证jwt合法性
     *
     * @param jwt
     * @return 账号ID
     */
    public static Long jwtVerified(String jwt) throws JwtInvalidException {
        try {
            String subject = Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody().getSubject();
            return Long.valueOf(subject);
        } catch (ExpiredJwtException e) {
            log.error("jwt {} 已过期", jwt);
            throw new JwtInvalidException("凭证已过期");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.error("不支持的jwt {}", jwt);
            throw new JwtInvalidException("凭证错误");
        } catch (SignatureException e) {
            log.error("jwt {} 签名错误", jwt);
            throw new JwtInvalidException("凭证错误");
        } catch (IllegalArgumentException e) {
            log.error("jwt {} 参数错误", jwt);
            throw new JwtInvalidException("凭证错误");
        }
    }

}
