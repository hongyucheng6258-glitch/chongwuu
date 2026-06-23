package com.petshop.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MongoDB 商店地理信息文档
 * 与 SQLite Store 表对应，扩展地理坐标和搜索功能
 */
@Data
@Document(collection = "store_geo")
public class StoreGeo {

    @Id
    private String id;

    /** 关联 SQLite Store ID */
    private Long storeId;

    /** 商店名称 */
    private String name;

    /** 详细地址 */
    private String address;

    /** 联系电话 */
    private String phone;

    /** 地理坐标 [经度, 纬度] - MongoDB GeoJSON Point */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    /** 纬度 */
    private BigDecimal latitude;

    /** 经度 */
    private BigDecimal longitude;

    /** 商店描述 */
    private String description;

    /** 图片URL */
    private String image;

    /** 营业时间 */
    private String businessHours;

    /** 状态: 1-营业中, 0-歇业 */
    private Integer status = 1;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public void setLocation(BigDecimal lat, BigDecimal lng) {
        if (lat != null && lng != null) {
            this.location = new GeoJsonPoint(lng.doubleValue(), lat.doubleValue());
            this.latitude = lat;
            this.longitude = lng;
        }
    }
}
