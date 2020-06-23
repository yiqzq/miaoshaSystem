package com.yiqzq.miaoshasystem.config;

import com.yiqzq.miaoshasystem.filter.ValidateCodeFilter;
import com.yiqzq.miaoshasystem.interceptor.LoginHandlerInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @author yiqzq
 * @date 2020/6/5 12:18
 */
@Configuration
public class MyServletConfig {


    //    @Bean
//    public FilterRegistrationBean<Filter> myFilter() {
//        FilterRegistrationBean<Filter> filterRegistrationBean =
//                new FilterRegistrationBean<Filter>();
//        //注册 过滤器
//        filterRegistrationBean.setFilter(new ValidateCodeFilter());
//        //设置拦截路径
//        filterRegistrationBean.setUrlPatterns(Arrays.asList("/user/register"));
//        return filterRegistrationBean;
//    }



}