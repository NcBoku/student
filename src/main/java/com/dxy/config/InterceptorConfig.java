package com.dxy.config;

import com.dxy.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    private LoginInterceptor loginInterceptor;


    @Value("${thk.imagesRealPath}")
    private String realPath;
    @Value("${thk.imagesServerPath}")
    private String serverPath;

    // 添加静态资源处理器
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{serverPath + "/**"}).addResourceLocations("file:/" + realPath);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建用户拦截器对象并指定其拦截的路径和排除的路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/upload");
        super.addInterceptors(registry);
    }
}

