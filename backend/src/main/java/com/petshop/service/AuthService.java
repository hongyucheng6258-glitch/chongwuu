package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.LoginRequest;
import com.petshop.dto.RegisterRequest;
import com.petshop.entity.User;
import com.petshop.repository.UserRepository;
import com.petshop.security.JwtUtil;
import com.petshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Map<String, Object> register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(PasswordUtil.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        userRepository.save(user);
        return buildLoginResult(user);
    }

    public Map<String, Object> login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        if (!PasswordUtil.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        return buildLoginResult(user);
    }

    private Map<String, Object> buildLoginResult(User user) {
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole(), user.getMemberLevel());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("role", user.getRole());
        userInfo.put("email", user.getEmail());
        userInfo.put("phone", user.getPhone());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("memberLevel", user.getMemberLevel());
        result.put("user", userInfo);
        return result;
    }
}
