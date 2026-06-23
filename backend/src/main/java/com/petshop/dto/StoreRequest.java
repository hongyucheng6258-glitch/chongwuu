package com.petshop.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StoreRequest {
    private String name;
    private String address;
    private String phone;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private String image;
    private String businessHours;
    private Integer status;
}
