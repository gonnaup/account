package org.gonnaup.accountmanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gonnaup.account.exception.JwtInvalidException;
import org.gonnaup.accountmanagement.annotation.JwtDataParam;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
import org.gonnaup.accountmanagement.domain.JwtData;
import org.gonnaup.accountmanagement.util.JWTUtil;
import org.gonnaup.accountmanagement.util.RequestUtil;
import org.gonnaup.accountmanagement.web.AccountInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author gonnaup
 * @version 2020/12/20 11:43
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    AccountInterceptor accountInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtDataHandlerMethodArgumentResolver());
    }

    /**
     * controller 应用名：AppName 参数解析<br/>
     * 参数带{@link ApplicationName}并且类型为{@link String}
     */
    @Slf4j
    public static class JwtDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            JwtDataParam annotation = parameter.getParameterAnnotation(JwtDataParam.class);
            return annotation != null && JwtData.class.isAssignableFrom(parameter.nestedIfOptional().getParameterType());
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            Object jwtData = Objects.requireNonNull(request).getAttribute(AuthenticateConst.REQUEST_ATTR_JWTDATA);
            if (jwtData == null) {
                if (log.isDebugEnabled()) {
                    log.debug("JwtData不存在，尝试从HttpServletRequest中解析");
                }
                try {
                    String jwt = RequestUtil.obtainJWT(request);
                    if (StringUtils.isBlank(jwt)) {
                        log.info("Jwt 为空，用户未登录");
                        return null;
                    }
                    jwtData = JWTUtil.jwtVerified(jwt);
                } catch (JwtInvalidException e) {
                    log.warn("获取jwtData参数失败");
                }
            }
            return jwtData;
        }
    }

}
