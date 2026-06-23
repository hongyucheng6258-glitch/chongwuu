package com.petshop.service;

import com.petshop.entity.Notification;
import com.petshop.entity.Order;
import com.petshop.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息通知服务
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 发送个人消息
     */
    @Transactional
    public Notification sendToUser(Long userId, String title, String content, String msgType, Long orderId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setMsgType(msgType);
        notification.setOrderId(orderId);
        return notificationRepository.save(notification);
    }

    /**
     * 发送广播消息（全员）
     */
    @Transactional
    public Notification broadcast(String title, String content, String msgType) {
        Notification notification = new Notification();
        notification.setUserId(null);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setMsgType(msgType);
        return notificationRepository.save(notification);
    }

    /**
     * 订单状态变更通知
     */
    @Transactional
    public void notifyOrderStatus(Order order, String statusText) {
        String title = "订单状态更新";
        String content = String.format("您的订单 %s 状态已更新为：%s", order.getOrderNo(), statusText);
        sendToUser(order.getUserId(), title, content, "ORDER", order.getId());
    }

    /**
     * 退单审核结果通知
     */
    @Transactional
    public void notifyReturnResult(Order order, boolean approved) {
        String title = approved ? "退单审核通过" : "退单审核驳回";
        String content = approved
                ? String.format("您的订单 %s 退单申请已通过，退款将原路返回。", order.getOrderNo())
                : String.format("您的订单 %s 退单申请未通过审核，订单已恢复原状态。", order.getOrderNo());
        sendToUser(order.getUserId(), title, content, "RETURN", order.getId());
    }

    /**
     * 获取用户消息列表
     */
    public List<Notification> listForUser(Long userId) {
        return notificationRepository.findByUserIdOrBroadcast(userId);
    }

    /**
     * 获取未读消息数量
     */
    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    /**
     * 标记消息为已读
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        // 只能标记自己的消息或广播消息
        if (notification.getUserId() != null && !notification.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        notification.setIsRead(1);
        notificationRepository.save(notification);
    }

    /**
     * 标记所有消息为已读
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findUnreadByUserId(userId);
        unread.forEach(n -> n.setIsRead(1));
        notificationRepository.saveAll(unread);
    }

    /**
     * 删除消息
     */
    @Transactional
    public void delete(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        if (notification.getUserId() != null && !notification.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        notificationRepository.delete(notification);
    }
}
