package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.entity.Notification;
import com.petshop.security.CurrentUser;
import com.petshop.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息通知接口
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /** 获取我的消息列表 */
    @GetMapping
    public ApiResponse<List<Notification>> list() {
        return ApiResponse.success(notificationService.listForUser(CurrentUser.getUserId()));
    }

    /** 获取未读消息数量 */
    @GetMapping("/unread-count")
    public ApiResponse<Map<String, Long>> unreadCount() {
        long count = notificationService.getUnreadCount(CurrentUser.getUserId());
        return ApiResponse.success(Map.of("count", count));
    }

    /** 标记消息为已读 */
    @PutMapping("/{id}/read")
    public ApiResponse<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id, CurrentUser.getUserId());
        return ApiResponse.success("已标记为已读", null);
    }

    /** 标记所有消息为已读 */
    @PutMapping("/read-all")
    public ApiResponse<?> markAllAsRead() {
        notificationService.markAllAsRead(CurrentUser.getUserId());
        return ApiResponse.success("全部已读", null);
    }

    /** 删除消息 */
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        notificationService.delete(id, CurrentUser.getUserId());
        return ApiResponse.success("删除成功", null);
    }
}
