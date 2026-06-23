package com.petshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Gitee 用户信息（从 Gitee API 获取）
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeUser {

    /** Gitee 用户 ID */
    private Long id;

    /** Gitee 用户名 */
    private String login;

    /** 昵称 */
    private String name;

    /** 头像 URL */
    private String avatar_url;

    /** 邮箱 */
    private String email;

    /** 个人博客 */
    private String blog;

    /** 个人简介 */
    private String bio;
}
