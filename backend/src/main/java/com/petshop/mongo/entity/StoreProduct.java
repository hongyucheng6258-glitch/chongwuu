package com.petshop.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MongoDB 商店商品关联文档
 * 记录商品属于哪个商店，支持不同商店有相同商品
 * 商品类型: PET-宠物(唯一单品), ACCESSORY-宠物周边(库存不限)
 */
@Data
@Document(collection = "store_products")
public class StoreProduct {

    @Id
    private String id;

    /** 关联 SQLite Product ID */
    private Long productId;

    /** 关联 SQLite Store ID */
    private Long storeId;

    /** 商店名称（冗余，方便查询） */
    private String storeName;

    /** 商品名称 */
    private String productName;

    /** 商品类型: PET / ACCESSORY */
    private String productType;

    /** 商品价格 */
    private BigDecimal price;

    /** 库存数量 (PET类型通常为1，ACCESSORY类型为实际库存) */
    private Integer stock;

    /** 商品描述 */
    private String description;

    /** 商品图片 */
    private String image;

    /** 状态: 1-上架, 0-下架 */
    private Integer status = 1;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
