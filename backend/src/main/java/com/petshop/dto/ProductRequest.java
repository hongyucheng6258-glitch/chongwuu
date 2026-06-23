package com.petshop.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String image;
    private Integer status;
}
