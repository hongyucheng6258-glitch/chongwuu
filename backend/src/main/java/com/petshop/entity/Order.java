package com.petshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    /** 商品原价总额 */
    @Column(name = "original_amount")
    private BigDecimal originalAmount;

    /** 优惠金额 */
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    /** 下单时会员等级 */
    @Column(name = "member_level")
    private Integer memberLevel;

    /**
     * 订单状态:
     *  0 - 已下单（待支付）
     *  1 - 已支付（待发货）
     *  2 - 已发货（待收货）
     *  3 - 已收货（待评价）
     *  4 - 已评价（订单流程结束）
     * -1 - 取消订单
     * -2 - 申请退单
     * -3 - 退单驳回（退回原状态，通过字段记录）
     * -4 - 管理员直接退单
     */
    @Column(nullable = false)
    private Integer status = 0;

    /** 原状态：退单驳回时用于恢复订单状态 */
    @Column(name = "previous_status")
    private Integer previousStatus;

    /** 取消/退单原因 */
    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    /** 退单理由 */
    @Column(name = "return_reason", length = 500)
    private String returnReason;

    /** 是否已评价: 0-未评价, 1-已评价 */
    @Column(name = "reviewed")
    private Integer reviewed = 0;

    private String receiver;

    private String phone;

    @Column(length = 500)
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
