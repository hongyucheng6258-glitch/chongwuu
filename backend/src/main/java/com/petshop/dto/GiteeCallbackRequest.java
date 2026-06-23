package com.petshop.dto;

import lombok.Data;

/**
 * Gitee OAuth2 回调请求
 */
@Data
public class GiteeCallbackRequest {

    /** Gitee 授权码 */
    private String code;

    /** 状态码（用于防 CSRF） */
    private String state;
}
