package com.petshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息通知实体
 */
@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 接收用户ID，null表示全员广播 */
    @Column(name = "user_id")
    private Long userId;

    /** 消息标题 */
    @Column(nullable = false)
    private String title;

    /** 消息内容 */
    @Column(nullable = false, length = 1000)
    private String content;

    /** 消息类型: ORDER-订单, SYSTEM-系统, PROMO-促销, RETURN-退单 */
    @Column(name = "msg_type")
    private String msgType = "SYSTEM";

    /** 关联订单ID */
    @Column(name = "order_id")
    private Long orderId;

    /** 是否已读: 0-未读, 1-已读 */
    @Column(name = "is_read")
    private Integer isRead = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isRead == null) {
            isRead = 0;
        }
    }
}
