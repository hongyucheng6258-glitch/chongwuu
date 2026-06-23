package com.petshop.dto;

import lombok.Data;

@Data
public class UserProfileRequest {
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
}
