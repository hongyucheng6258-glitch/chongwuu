package com.petshop.dto;

import lombok.Data;

@Data
public class VideoRequest {
    private String title;
    private String description;
    private String videoUrl;
    private String coverUrl;
    private Long productId;
    private String category;
}
