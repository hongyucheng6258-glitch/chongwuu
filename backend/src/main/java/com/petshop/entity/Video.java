package com.petshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    /** 视频URL */
    @Column(name = "video_url")
    private String videoUrl;

    /** 封面图 */
    @Column(name = "cover_url")
    private String coverUrl;

    /** 关联商品ID */
    @Column(name = "product_id")
    private Long productId;

    /** 分类: PET_CARE-宠物护理, PET_FOOD-喂养指南, PET_PLAY-萌宠日常, PRODUCT_INTRO-商品介绍 */
    @Column(name = "category")
    private String category;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
