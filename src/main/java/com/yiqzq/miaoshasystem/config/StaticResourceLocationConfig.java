package com.yiqzq.miaoshasystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yiqzq
 * @date 2020/6/4 14:19
 */
@Configuration
public class StaticResourceLocationConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
        //第一个方法设置访问路径前缀，第二个方法设置资源路径
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        //windows引用
//        registry.addResourceHandler("/userimg/**").addResourceLocations("file:F:/image/");
        //linux引用
        registry.addResourceHandler("/userimg/**").addResourceLocations("file:/usr/local/img/");
    }


}
