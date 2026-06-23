package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.dto.UserProfileRequest;
import com.petshop.entity.User;
import com.petshop.security.CurrentUser;
import com.petshop.security.JwtUtil;
import com.petshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ApiResponse<User> profile() {
        return ApiResponse.success(userService.getProfile());
    }

    @PutMapping("/profile")
    public ApiResponse<User> updateProfile(@RequestBody UserProfileRequest req) {
        return ApiResponse.success("个人信息已更新", userService.updateProfile(req));
    }

    @GetMapping("/admin/list")
    public ApiResponse<List<User>> adminList() {
        checkAdmin();
        return ApiResponse.success(userService.listAll());
    }

    @PutMapping("/admin/{id}/member-level")
    public ApiResponse<Map<String, Object>> updateMemberLevel(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> body) {
        checkAdmin();
        User updatedUser = userService.updateMemberLevel(id, body.get("level"));

        // 如果修改的是当前登录用户，生成新token
        Map<String, Object> result = new HashMap<>();
        result.put("user", updatedUser);
        if (CurrentUser.getUserId().equals(id)) {
            String newToken = jwtUtil.generateToken(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getRole(), updatedUser.getMemberLevel());
            result.put("token", newToken);
        }
        return ApiResponse.success("会员等级已更新", result);
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
