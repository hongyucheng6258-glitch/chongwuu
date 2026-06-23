package com.petshop.security;

import com.petshop.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.entity.User;
import com.petshop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器 - 校验登录状态
 * 对于需要认证的路径：无token返回401
 * 对于公开路径：有token则解析用户上下文（用于会员折扣等），无token则放行
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 公开路径（白名单中的路径），这些路径无token也放行 */
    private boolean isPublicPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // 商品相关公开接口
        if (uri.startsWith("/api/products/list") || uri.startsWith("/api/products/detail/")
                || uri.startsWith("/api/products/search")) return true;
        // 分类
        if (uri.startsWith("/api/categories/list")) return true;
        // 商店
        if (uri.startsWith("/api/stores/list") || uri.startsWith("/api/stores/search")
                || uri.startsWith("/api/stores/nearby") || uri.startsWith("/api/stores/detail/")) return true;
        // 视频
        if (uri.startsWith("/api/videos/list") || uri.startsWith("/api/videos/detail/")
                || uri.startsWith("/api/videos/product/") || uri.startsWith("/api/videos/category/")) return true;
        // 推荐
        if (uri.startsWith("/api/recommend/similar/") || uri.startsWith("/api/recommend/hot")) return true;
        // AI
        if (uri.startsWith("/api/ai/chat")) return true;
        // 评价
        if (uri.startsWith("/api/reviews/product/")) return true;
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 预检
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserId(token);
                String username = jwtUtil.parseToken(token).get("username", String.class);
                String role = jwtUtil.getRole(token);
                // 从数据库查询最新的会员等级（支持后台修改后实时生效）
                Integer memberLevel = jwtUtil.getMemberLevel(token);
                try {
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null && user.getMemberLevel() != null) {
                        memberLevel = user.getMemberLevel();
                    }
                } catch (Exception e) {
                    // 数据库查询失败时使用token中的值
                }
                CurrentUser.set(new CurrentUser.UserContext(userId, username, role, memberLevel));
                return true;
            }
        }

        // 公开路径：无token也放行
        if (isPublicPath(request)) {
            return true;
        }

        // 需要认证的路径：未登录返回401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.error(401, "未登录或登录已过期")));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrentUser.clear();
    }
}
