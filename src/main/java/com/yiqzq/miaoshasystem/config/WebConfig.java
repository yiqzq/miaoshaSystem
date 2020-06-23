package com.yiqzq.miaoshasystem.config;

import com.yiqzq.miaoshasystem.interceptor.AccessLimitInterceptor;
import com.yiqzq.miaoshasystem.interceptor.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yiqzq
 * @date 2020/6/6 23:45
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/", "/index.html", "/user/login")
                .excludePathPatterns("/test/**")
                .excludePathPatterns("/user/register", "/user_register.html", "/code/image")
                .excludePathPatterns("/static/**", "webjars/**")
                .excludePathPatterns("/js/**", "/layer/**")
//                .excludePathPatterns("/goods_detail.htm", "/order_detail.htm")
                .excludePathPatterns("/bootstrap/**", "/css/**", "/img/**", "/jquery-validation/**");
        registry.addInterceptor(new AccessLimitInterceptor()).addPathPatterns("/**");
    }
}
