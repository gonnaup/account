package org.gonnaup.accountmanagement.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.accountmanagement.constant.ApplicationName;
import org.gonnaup.accountmanagement.domain.JwtData;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
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

    private static final String key = "GONNAUP_NNKDLSLKKI_19949930299904903940349309403949";

    private static final Key SECRET_KEY;

    static {
        SECRET_KEY = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

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
    public static JwtData jwtVerified(String jwt) throws JwtInvalidException {
        if (StringUtils.isBlank(jwt)) {
            log.warn("jwt为空，用户未登录!");
            throw new JwtInvalidException("您还未登录");
        }
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            Long subject = Long.valueOf(claims.getSubject());
            String appName = claims.getIssuer();
            Long remainder = claims.getExpiration().getTime() - System.currentTimeMillis();//到期时间 - 现在时间 = 剩余毫秒数
            return JwtData.of(subject, appName, remainder);
        } catch (ExpiredJwtException e) {
            log.warn("jwt {} 已过期", jwt);
            throw new JwtInvalidException("登录凭证已过期");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.warn("不支持的jwt {}", jwt);
            throw new JwtInvalidException("登录凭证错误");
        } catch (SignatureException e) {
            log.warn("jwt {} 签名错误", jwt);
            throw new JwtInvalidException("登录凭证错误");
        } catch (IllegalArgumentException e) {
            log.warn("jwt {} 参数错误", jwt);
            throw new JwtInvalidException("登录凭证错误");
        }
    }

}
