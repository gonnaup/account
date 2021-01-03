package org.gonnaup.accountmanagement.config;

import org.gonnaup.account.annotation.ApplicationNameParam;
import org.gonnaup.accountmanagement.constant.AuthenticateConst;
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
        resolvers.add(new AppNameHandlerMethodArgumentResolver());
    }

    /**
     * controller AppName 参数解析<br/>
     * 参数带{@link ApplicationNameParam}并且类型为{@link String}
     */
    public static class AppNameHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            ApplicationNameParam annotation = parameter.getParameterAnnotation(ApplicationNameParam.class);
            return annotation != null && String.class.isAssignableFrom(parameter.nestedIfOptional().getParameterType());
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            return Objects.requireNonNull(request).getAttribute(AuthenticateConst.REQUEST_ATTR_APPNAME);
        }
    }

}
