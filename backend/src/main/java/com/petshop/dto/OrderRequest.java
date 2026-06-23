package com.petshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    private List<Long> cartItemIds;

    @NotBlank(message = "收货人不能为空")
    private String receiver;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "收货地址不能为空")
    private String address;
}
