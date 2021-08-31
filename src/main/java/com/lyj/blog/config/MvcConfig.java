package com.lyj.blog.config;

import com.lyj.blog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/14 2:46 下午
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 拦截器的路径排除：先循环进行排除的正则校验，如果匹配，则直接返回false；排除的规则遍历完后，进行拦截的正则校验
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**");//拦截所有路径
        //排除静态资源文件(排除的要根据请求频率从高到低排，底层实现实际上就是循环进行正则校验，如果匹配，则返回false，不继续下一个拦截器)
        registration.excludePathPatterns(
                "/**/*.html",
                "/**/*.js", "/**/*.css", "/favicon.ico", "/**/*.png", "/**/*.jpg",
                "/**/*.gif", "/**/*.map", "/file/**", "/**/*.ttf",
                "/**/*.woff", "/**/*.woff2", "/**/*.svg");
    }
}
