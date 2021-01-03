package org.gonnaup.accountmanagement.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * HttpServletRequest工具类
 *
 * @author gonnaup
 * @version 2021/1/1 14:18
 */
@Slf4j
public class RequestUtil {

    private RequestUtil() {
    }

    /**
     * 从http request中获取jwt值
     * @param request
     * @return jwt值
     */
    public static String obtainJWT(HttpServletRequest request) {
        Objects.requireNonNull(request, "request 不能为空");
        return request.getHeader(AuthenticateConst.JWT_HEADER_NAME);
    }

    /**
     * 获取用户真实IP地址，服务经过多层代理时，会将真实IP放在请求头中
     * <ul>
     *     <li>"X-Forwarded-For"头格式：realIP,proxyIP1,proxyIP2... </li>
     *     <li>"X-Real_IP"头格式：realIP</li>
     * </ul>
     * 其他的待定
     */
    public static String obtainRealIpAddr(HttpServletRequest request) {
        Objects.requireNonNull(request, "request 不能为空");
        String ip = request.getHeader("X-Forwarded-For");
        if (ipExists(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0].trim();
            }
        }
        if (!ipExists(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!ipExists(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    private static boolean ipExists(String ip) {
        return StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip);
    }


}
