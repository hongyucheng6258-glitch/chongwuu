package com.petshop.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 密码加密工具 (SHA-256 + 盐)
 */
public class PasswordUtil {

    private static final String SALT = "petshop-2024-salt";

    public static String encode(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((raw + SALT).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    public static boolean matches(String raw, String encoded) {
        return encode(raw).equals(encoded);
    }
}
