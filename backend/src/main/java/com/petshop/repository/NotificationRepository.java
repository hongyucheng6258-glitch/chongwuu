package com.petshop.repository;

import com.petshop.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 查询用户的消息（个人消息+广播消息），按时间倒序
     */
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId OR n.userId IS NULL ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdOrBroadcast(@Param("userId") Long userId);

    /**
     * 查询用户的未读消息数量
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE (n.userId = :userId OR n.userId IS NULL) AND n.isRead = 0")
    long countUnreadByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的未读消息
     */
    @Query("SELECT n FROM Notification n WHERE (n.userId = :userId OR n.userId IS NULL) AND n.isRead = 0 ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUserId(@Param("userId") Long userId);
}
