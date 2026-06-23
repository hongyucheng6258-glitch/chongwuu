package com.petshop.dto;

import lombok.Data;

@Data
public class OrderReturnRequest {
    /** 退单理由 */
    private String reason;
}
