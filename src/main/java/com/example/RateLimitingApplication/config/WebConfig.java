package com.example.RateLimitingApplication.config;

import com.example.RateLimitingApplication.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // This tells Spring to apply our bouncer to all /api/ endpoints
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/**");
    }

}
