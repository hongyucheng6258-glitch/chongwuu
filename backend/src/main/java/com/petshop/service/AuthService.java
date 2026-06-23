package com.petshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.common.BusinessException;
import com.petshop.dto.GiteeUser;
import com.petshop.dto.LoginRequest;
import com.petshop.dto.RegisterRequest;
import com.petshop.entity.User;
import com.petshop.repository.UserRepository;
import com.petshop.security.JwtUtil;
import com.petshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gitee.client-id}")
    private String giteeClientId;

    @Value("${gitee.client-secret}")
    private String giteeClientSecret;

    @Value("${gitee.redirect-uri}")
    private String giteeRedirectUri;

    /** Redis 中验证码的 key 前缀 */
    private static final String SMS_CODE_PREFIX = "sms:code:";
    /** 验证码有效期：5 分钟 */
    private static final long SMS_CODE_EXPIRE = 5;
    /** 发送验证码的冷却时间：60 秒 */
    private static final long SMS_COOLDOWN = 60;

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

    /**
     * Gitee 第三方登录
     * 1. 用授权码换取 access_token
     * 2. 用 access_token 获取 Gitee 用户信息
     * 3. 查找或自动创建本地用户
     * 4. 返回 JWT token
     */
    public Map<String, Object> loginWithGitee(String code) {
        // 第一步：用 code 换取 access_token
        String accessToken = getGiteeAccessToken(code);

        // 第二步：用 access_token 获取 Gitee 用户信息
        GiteeUser giteeUser = getGiteeUserInfo(accessToken);

        // 第三步：查找或创建本地用户
        User user = userRepository.findByGiteeId(giteeUser.getId()).orElse(null);

        if (user == null) {
            // 首次 Gitee 登录，自动创建用户
            String username = "gitee_" + giteeUser.getLogin();
            // 确保用户名唯一
            String finalUsername = username;
            int suffix = 1;
            while (userRepository.existsByUsername(finalUsername)) {
                finalUsername = username + suffix++;
            }

            user = new User();
            user.setUsername(finalUsername);
            // 设置一个随机密码（Gitee 登录不需要密码）
            user.setPassword(PasswordUtil.encode("gitee_oauth_" + System.currentTimeMillis()));
            user.setNickname(giteeUser.getName() != null ? giteeUser.getName() : giteeUser.getLogin());
            user.setAvatar(giteeUser.getAvatar_url());
            user.setEmail(giteeUser.getEmail());
            user.setGiteeId(giteeUser.getId());
            userRepository.save(user);
        } else {
            // 已关联用户，更新头像和昵称
            if (giteeUser.getAvatar_url() != null) {
                user.setAvatar(giteeUser.getAvatar_url());
            }
            if (giteeUser.getName() != null) {
                user.setNickname(giteeUser.getName());
            }
            userRepository.save(user);
        }

        return buildLoginResult(user);
    }

    /**
     * 通过授权码获取 Gitee access_token
     */
    private String getGiteeAccessToken(String code) {
        String tokenUrl = "https://gitee.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("code", code);
        requestBody.put("client_id", giteeClientId);
        requestBody.put("client_secret", giteeClientSecret);
        requestBody.put("redirect_uri", giteeRedirectUri);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            System.out.println("[Gitee Debug] 请求 token URL: " + tokenUrl);
            System.out.println("[Gitee Debug] client_id: " + giteeClientId);
            System.out.println("[Gitee Debug] redirect_uri: " + giteeRedirectUri);
            System.out.println("[Gitee Debug] code: " + code);

            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            System.out.println("[Gitee Debug] 响应状态码: " + response.getStatusCode());
            System.out.println("[Gitee Debug] 响应体: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String body = response.getBody();
                // 尝试 JSON 格式解析
                Map<String, Object> result = objectMapper.readValue(body, Map.class);
                if (result.containsKey("access_token")) {
                    return (String) result.get("access_token");
                }
                // 尝试表单格式解析
                for (String pair : body.split("&")) {
                    String[] kv = pair.split("=");
                    if (kv.length >= 2 && "access_token".equals(kv[0])) {
                        return kv[1];
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[Gitee Debug] 异常: " + e.getClass().getName() + " - " + e.getMessage());
            throw new BusinessException("Gitee 授权失败: " + e.getMessage());
        }
        throw new BusinessException("获取 Gitee access_token 失败");
    }

    /**
     * 通过 access_token 获取 Gitee 用户信息
     */
    private GiteeUser getGiteeUserInfo(String accessToken) {
        String userInfoUrl = "https://gitee.com/api/v5/user?access_token=" + accessToken;

        try {
            ResponseEntity<GiteeUser> response = restTemplate.getForEntity(userInfoUrl, GiteeUser.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            throw new BusinessException("获取 Gitee 用户信息失败: " + e.getMessage());
        }
        throw new BusinessException("获取 Gitee 用户信息失败");
    }

    /**
     * 发送短信验证码（模拟发送，验证码直接返回并在控制台打印）
     */
    public String sendSmsCode(String phone) {
        // 检查冷却时间
        String cooldownKey = SMS_CODE_PREFIX + "cooldown:" + phone;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(cooldownKey))) {
            throw new BusinessException("发送过于频繁，请稍后再试");
        }

        // 生成 6 位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 存入 Redis，有效期 5 分钟
        String codeKey = SMS_CODE_PREFIX + phone;
        stringRedisTemplate.opsForValue().set(codeKey, code, SMS_CODE_EXPIRE, TimeUnit.MINUTES);

        // 设置冷却时间 60 秒
        stringRedisTemplate.opsForValue().set(cooldownKey, "1", SMS_COOLDOWN, TimeUnit.SECONDS);

        // 模拟发送：在控制台打印验证码
        System.out.println("========================================");
        System.out.println("【短信验证码】手机号: " + phone);
        System.out.println("【短信验证码】验证码: " + code);
        System.out.println("【短信验证码】有效期: 5 分钟");
        System.out.println("========================================");

        return code;
    }

    /**
     * 短信验证码登录
     */
    public Map<String, Object> smsLogin(String phone, String code) {
        // 从 Redis 中获取验证码
        String codeKey = SMS_CODE_PREFIX + phone;
        String storedCode = stringRedisTemplate.opsForValue().get(codeKey);

        if (storedCode == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }
        if (!storedCode.equals(code)) {
            throw new BusinessException("验证码错误");
        }

        // 验证码正确，删除 Redis 中的验证码（一次性使用）
        stringRedisTemplate.delete(codeKey);

        // 查找用户：优先通过手机号查找
        User user = userRepository.findAll().stream()
                .filter(u -> phone.equals(u.getPhone()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            // 手机号未绑定任何用户，自动创建新用户
            String username = "phone_" + phone.substring(phone.length() - 4);
            String finalUsername = username;
            int suffix = 1;
            while (userRepository.existsByUsername(finalUsername)) {
                finalUsername = username + suffix++;
            }

            user = new User();
            user.setUsername(finalUsername);
            user.setPassword(PasswordUtil.encode("sms_login_" + System.currentTimeMillis()));
            user.setPhone(phone);
            user.setNickname("手机用户" + phone.substring(phone.length() - 4));
            userRepository.save(user);
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
