package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.dto.GiteeCallbackRequest;
import com.petshop.dto.LoginRequest;
import com.petshop.dto.RegisterRequest;
import com.petshop.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${gitee.client-id}")
    private String giteeClientId;

    @Value("${gitee.redirect-uri}")
    private String giteeRedirectUri;

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody RegisterRequest req) {
        return ApiResponse.success("注册成功", authService.register(req));
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.success("登录成功", authService.login(req));
    }

    /**
     * 获取 Gitee 授权 URL
     */
    @GetMapping("/gitee/authorize-url")
    public ApiResponse<Map<String, String>> getGiteeAuthorizeUrl() {
        String url = "https://gitee.com/oauth/authorize?client_id=" + giteeClientId
                + "&redirect_uri=" + giteeRedirectUri
                + "&response_type=code&scope=user_info%20emails";
        return ApiResponse.success("获取成功", Map.of("authorizeUrl", url));
    }

    /**
     * Gitee OAuth2 回调处理
     */
    @GetMapping("/gitee/callback")
    public void giteeCallback(@ModelAttribute GiteeCallbackRequest req, HttpServletResponse response) throws IOException {
        String code = req.getCode();
        if (code == null || code.isEmpty()) {
            response.sendRedirect("http://localhost:5173/login?error=gitee_auth_failed");
            return;
        }
        try {
            Map<String, Object> result = authService.loginWithGitee(code);
            String token = (String) result.get("token");
            String userJson = java.net.URLEncoder.encode(
                    new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(result.get("user")),
                    java.nio.charset.StandardCharsets.UTF_8
            );
            response.sendRedirect("http://localhost:5173/login?giteeLogin=1&token=" + token + "&user=" + userJson);
        } catch (Exception e) {
            response.sendRedirect("http://localhost:5173/login?error=gitee_login_failed&msg=" +
                    java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
        }
    }
}
