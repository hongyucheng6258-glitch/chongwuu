package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.dto.LoginRequest;
import com.petshop.dto.RegisterRequest;
import com.petshop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody RegisterRequest req) {
        return ApiResponse.success("注册成功", authService.register(req));
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.success("登录成功", authService.login(req));
    }
}
