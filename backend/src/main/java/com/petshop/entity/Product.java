package com.petshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "category_id")
    private Long categoryId;

    /** 归属商店ID */
    @Column(name = "store_id")
    private Long storeId;

    /** 商品类型: PET-宠物类(单品唯一), ACCESSORY-宠物周边类(库存无限制) */
    @Column(name = "product_type")
    private String productType = "ACCESSORY";

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 2000)
    private String description;

    private String image;

    /** 关联视频ID */
    @Column(name = "video_id")
    private Long videoId;

    /** 状态: 1 上架, 0 下架 */
    @Column(nullable = false)
    private Integer status = 1;

    /** 平均评分 */
    private Double rating;

    /** 会员折扣价格 (非持久化字段) */
    @Transient
    private java.math.BigDecimal discountPrice;

    /** 会员折扣率 (非持久化字段) */
    @Transient
    private java.math.BigDecimal discountRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    @JsonIgnore
    private Store store;
}
