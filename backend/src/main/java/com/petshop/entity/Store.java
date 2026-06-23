package com.petshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    private String phone;

    /** 纬度 */
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    /** 经度 */
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(length = 1000)
    private String description;

    private String image;

    /** 营业时间 */
    @Column(name = "business_hours")
    private String businessHours;

    /** 状态: 1-营业中, 0-歇业 */
    private Integer status = 1;
}
