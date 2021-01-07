package org.gonnaup.accountmanagement.web;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.domain.RoleTree;
import org.gonnaup.account.exception.AuthenticationException;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.accountmanagement.annotation.RequireLogin;
import org.gonnaup.accountmanagement.annotation.RequirePermission;
import org.gonnaup.accountmanagement.annotation.RequireRole;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.enums.PermissionType;
import org.gonnaup.accountmanagement.enums.RoleType;
import org.gonnaup.accountmanagement.service.AccountRoleService;
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
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private AccountRoleService accountRoleService;

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
            if (obtainRequireLogin(handlerMethod, RequireLogin.class) != null) {
                handleRequireLogin(request);
            }
    //TODO 重写权限验证逻辑
            //需要某角色才能访问
            RequireRole requireRole = null;
            if ((requireRole = obtainRequireLogin(handlerMethod, RequireRole.class)) != null) {
                handleRequireRole(request, requireRole);
            }

            //需要某特定权限才能访问
            RequirePermission requirePermission = null;
            if ((requirePermission = obtainRequireLogin(handlerMethod, RequirePermission.class)) != null) {
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
    private <T extends Annotation> T obtainRequireLogin(HandlerMethod handlerMethod, Class<T> clazz) {
        T methodLevel = handlerMethod.getMethodAnnotation(clazz);
        //RequirePermission只支持方法级别
        if (methodLevel != null || clazz == RequirePermission.class) {
            return methodLevel;
        }
        return handlerMethod.getBeanType().getAnnotation(clazz);
    }

    /**
     * 验证登录状态是否合法
     *
     * @param request
     * @param requireLogin
     * @throws JwtInvalidException 登录状态验证失败
     */
    protected void handleRequireLogin(HttpServletRequest request) throws JwtInvalidException {
        jwtDataThreadLocal.set(obtainValidJwt(request));
    }

    /**
     * 验证角色是否拥有角色
     *
     * @param request
     * @param requireRole
     * @return 拥有角色返回 <code>true</code>，未登录或未拥有角色返回 <code>false</code>
     * @throws JwtInvalidException     登录状态验证失败
     * @throws AuthenticationException 角色权限验证失败
     */
    protected void handleRequireRole(HttpServletRequest request, RequireRole requireRole) throws JwtInvalidException, AuthenticationException {
        JwtData jwtData = jwtDataThreadLocal.get();
        if (jwtData == null) {
            jwtDataThreadLocal.set(jwtData = obtainValidJwt(request));//获取jwt账户信息
        }
        List<String> rolesRequired = Arrays.stream(requireRole.value()).map(RoleType::name).collect(Collectors.toUnmodifiableList());//必需角色
        Long accountId = jwtData.getAccountId();
        final Set<String> rolesOwned = Sets.newHashSet(accountRoleService.findRoleNamesByAccountId(accountId));//账户拥有的角色列表
        //账号包含任意的一个高级角色或包含所有必需角色
        if (rolesOwned.containsAll(rolesRequired)) {
            //包含所有需要的角色，验证通过
            if (log.isDebugEnabled()) {
                log.debug("账户[{}] 角色验证通过，需要{}，拥有{}", accountId, rolesRequired, rolesOwned);
            }
        } else {
            //存在未拥有的角色
            if (log.isDebugEnabled()) {
                //需要但未拥有的角色
                List<String> rolesNotOwned = rolesRequired.stream().filter(Predicate.not(rolesOwned::contains)).collect(Collectors.toList());
                log.debug("账户[{}] 没有{}角色，无法访问{}", accountId, rolesNotOwned, request.getServletPath());
            }
            throw new AuthenticationException("用户权限不够!");

        }

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
        List<String> permissionRequired = Arrays.stream(requirePermission.value()).map(PermissionType::name).collect(Collectors.toUnmodifiableList());//必需包含的权限
        Long accountId = jwtData.getAccountId();
        List<RoleTree> roleTrees = accountRoleService.findRoleTreesByAccountId(accountId, jwtData.getAppName());
        Set<String> permissionOwned = roleTrees.stream().flatMap(roleTree -> roleTree.getPermissionNameSet().stream()).collect(Collectors.toSet());//账号拥有的权限
        //包含其中任意一个高级权限或包含所有必需权限
        if (permissionOwned.containsAll(permissionRequired)) {
            //包含所有需要的权限
            if (log.isDebugEnabled()) {
                log.debug("账户[{}]权限验证通过，需要{}，拥有{}", accountId, permissionRequired, permissionOwned);
            }
        } else {
            //存在未拥有的权限
            if (log.isDebugEnabled()) {
                List<String> permissionNotOwned = permissionRequired.stream().filter(Predicate.not(permissionOwned::contains)).collect(Collectors.toList());
                log.debug("账户[{}]没有{}权限，无法访问{}", accountId, permissionNotOwned, request.getServletPath());
            }
            throw new AuthenticationException("用户权限不够!");
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
