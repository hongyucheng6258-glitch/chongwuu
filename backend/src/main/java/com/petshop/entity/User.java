package com.petshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String phone;

    private String nickname;

    private String avatar;

    /** 会员等级: 0-普通用户, 1-银卡会员, 2-金卡会员, 3-钻石会员 */
    @Column(name = "member_level")
    private Integer memberLevel = 0;

    /** 角色: USER 普通用户, ADMIN 管理员 */
    @Column(nullable = false)
    private String role = "USER";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Gitee 第三方登录关联 ID（通过数据库索引保证唯一） */
    @Column(name = "gitee_id")
    private Long giteeId;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (memberLevel == null) {
            memberLevel = 0;
        }
    }
}
