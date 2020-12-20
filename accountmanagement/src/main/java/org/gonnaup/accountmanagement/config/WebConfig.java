package org.gonnaup.accountmanagement.config;

import org.gonnaup.accountmanagement.web.interceptor.AccountInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
}
