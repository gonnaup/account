package org.gonnaup.accountmanagement.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.gonnaup.accountmanagement.service.RolePermissionConfirmService;
import org.gonnaup.accountmanagement.util.JWTUtil;
import org.gonnaup.accountmanagement.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * 账号拦截器，进行账号验证和权限控制
 *
 * @author gonnaup
 * @version 2020/12/20 9:51
 */
@Slf4j
@Component
public class AccountInterceptor implements HandlerInterceptor {

    @Autowired
    RolePermissionConfirmService rolePermissionConfirmService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * JwtData threadLocal，可防止重复验证jwt
     */
    private final ThreadLocal<JwtData> jwtDataThreadLocal = new ThreadLocal<>();

    /**
     * 账户角色权限控制拦截
     * <p>
     *     <ul>
     *         <li>验证顺序：登录状态 -> 角色 -> 权限</li>
     *         <li>三种规则可同时使用，全部验证通过则验证成功</li>
     *     </ul>
     * </p>
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws JwtInvalidException     登录状态验证失败
     * @throws AuthenticationException 角色权限验证失败
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JwtInvalidException, AuthenticationException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //需要登录才能访问
            if (obtainAnnotation(handlerMethod, RequireLogin.class) != null) {
                handleRequireLogin(request);
            }
            //需要某特定权限才能访问
            RequirePermission requirePermission = null;
            if ((requirePermission = obtainAnnotation(handlerMethod, RequirePermission.class)) != null) {
                handleRequirePermission(request, requirePermission);
            }

            //将用户所属appName存入request中
            JwtData jwtData = jwtDataThreadLocal.get();
            //TODO 判断jwt的剩余时间，达到一定阀值之后返回重签信息
            if (jwtData != null) {
                request.setAttribute(AuthenticateConst.REQUEST_ATTR_JWTDATA, jwtData);//缓存JwtData
                jwtDataThreadLocal.remove();//移除jwt数据
            }
        }
        return true;
    }

    /**
     * 判断方法上和方法所在类上是否有指定的注解，<b>方法级别优先级大于类级别</b>
     *
     * @param handlerMethod
     * @param <T>           注解泛型
     * @return 如果有则返回此注解，负责返回<code>null</code>
     */
    private <T extends Annotation> T obtainAnnotation(HandlerMethod handlerMethod, Class<T> clazz) {
        T methodLevel = handlerMethod.getMethodAnnotation(clazz);
        return methodLevel == null ? handlerMethod.getBeanType().getAnnotation(clazz) : methodLevel;
    }

    /**
     * 验证登录状态是否合法
     *
     * @param request
     * @param requireLogin
     * @throws JwtInvalidException 登录状态验证失败
     */
    protected void handleRequireLogin(HttpServletRequest request) throws JwtInvalidException {
        if (log.isDebugEnabled()) {
            log.debug("开始验证用户是否登录");
        }
        jwtDataThreadLocal.set(obtainValidJwt(request));
    }

    /**
     * 验证是否具有指定权限
     *
     * @param request
     * @param requirePermission
     * @throws JwtInvalidException     登录状态验证失败
     * @throws AuthenticationException 角色权限验证失败
     */
    protected void handleRequirePermission(HttpServletRequest request, RequirePermission requirePermission) throws JwtInvalidException, AuthenticationException {
        JwtData jwtData = jwtDataThreadLocal.get();
        if (jwtData == null) {
            jwtDataThreadLocal.set(jwtData = obtainValidJwt(request));//获取jwt账户信息
        }
        if (log.isDebugEnabled()) {
            log.debug("开始对账户[{}]进行权限验证", jwtData.getAccountId());
        }
        int[] permissionScores = Arrays.stream(requirePermission.permissions()).mapToInt(PermissionType::weight).toArray();
        int[] roleScores = Arrays.stream(requirePermission.roles()).mapToInt(RoleType::score).toArray();
        int[] commonScores = requirePermission.scores();
        if (!rolePermissionConfirmService.hasPermission(jwtData.getAccountId(), ArrayUtils.addAll(ArrayUtils.addAll(permissionScores, roleScores), commonScores))) {
            //存在未拥有的权限
            if (log.isDebugEnabled()) {
                log.debug("账户[{}]无权访问api[{}]", jwtData.getAccountId(), request.getServletPath());
            }
            throw new AuthenticationException("用户权限不够!");
        } else if (log.isDebugEnabled()) {
            log.debug("账户[{}]权限验证通过", jwtData.getAccountId());
        }
    }

    /**
     * 获取并验证jwt
     *
     * @param request
     * @return jwt信息数据
     * @throws JwtInvalidException 验证不通过抛出
     */
    protected JwtData obtainValidJwt(HttpServletRequest request) throws JwtInvalidException {
        String jwt = RequestUtil.obtainJWT(request);
        if (redisTemplate.opsForValue().get(AuthenticateConst.JWT_BLACKLIST_REDIS_PREFIX + jwt) != null) {
            log.info("jwt {} 已被注销", jwt);
            throw new JwtInvalidException("登录凭证已过期");
        }
        return JWTUtil.jwtVerified(jwt);
    }

}
