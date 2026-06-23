package com.petshop.config;

import com.petshop.security.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册 - 配置需要鉴权的路径
 */
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        // 认证
                        "/api/auth/login",
                        "/api/auth/register",
                        // Gitee 第三方登录
                        "/api/auth/gitee/callback",
                        "/api/auth/gitee/authorize-url"
                        // 其他公开路径在 JwtInterceptor 内部处理（支持携带token时解析用户上下文）
                );
    }
}
