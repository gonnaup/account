package org.gonnaup.accountmanagement.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.gonnaup.accountmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 账号拦截器，进行账号验证和权限控制
 * @author gonnaup
 * @version 2020/12/20 9:51
 */
@Component
@Slf4j
public class AccountInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(handler.getClass().getName());
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
        }
        return true;
    }
}
