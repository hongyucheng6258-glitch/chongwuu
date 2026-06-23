package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.UserProfileRequest;
import com.petshop.entity.User;
import com.petshop.repository.UserRepository;
import com.petshop.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 获取当前用户个人信息
     */
    public User getProfile() {
        return userRepository.findById(CurrentUser.getUserId())
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    /**
     * 更新个人信息
     */
    @Transactional
    public User updateProfile(UserProfileRequest req) {
        User user = userRepository.findById(CurrentUser.getUserId())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (req.getNickname() != null) user.setNickname(req.getNickname());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getAvatar() != null) user.setAvatar(req.getAvatar());
        return userRepository.save(user);
    }

    /**
     * 管理员获取所有用户列表
     */
    public java.util.List<User> listAll() {
        return userRepository.findAll();
    }

    /**
     * 管理员更新用户会员等级
     */
    @Transactional
    public User updateMemberLevel(Long userId, Integer level) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setMemberLevel(level);
        return userRepository.save(user);
    }
}
