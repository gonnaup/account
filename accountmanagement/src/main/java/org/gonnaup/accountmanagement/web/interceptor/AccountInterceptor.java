package org.gonnaup.accountmanagement.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.account.annotation.RequireLogin;
import org.gonnaup.account.annotation.RequirePermission;
import org.gonnaup.account.annotation.RequireRole;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.service.AccountService;
import org.gonnaup.accountmanagement.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 账号拦截器，进行账号验证和权限控制
 *
 * @author gonnaup
 * @version 2020/12/20 9:51
 */
@Component
@Slf4j
public class AccountInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JwtInvalidException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            boolean next = false;
            //需要登录才能访问
            if (handlerMethod.hasMethodAnnotation(RequireLogin.class)) {
                String jwt = request.getHeader(AuthenticateConst.JWT_HEADER_NAME);
                if (jwt == null) {
                    throw new JwtInvalidException("用户未登录");
                } else {
                    JWTUtil.jwtVerified(jwt);//验证jwt合法性
                    next = true;
                }
            }
            RequireRole requireRole = null;
            if ((requireRole = handlerMethod.getMethodAnnotation(RequireRole.class)) != null) {
                List<String> roles = Arrays.asList(requireRole.value());//需要的角色


            }

            if (handlerMethod.hasMethodAnnotation(RequirePermission.class)) {

            }
            return next;

        }
        return true;
    }
}
