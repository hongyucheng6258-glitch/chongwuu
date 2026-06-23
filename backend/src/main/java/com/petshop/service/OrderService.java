package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.OrderRequest;
import com.petshop.entity.*;
import com.petshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MemberDiscountService memberDiscountService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final RecommendationService recommendationService;

    /**
     * 从购物车下单（状态0:已下单待支付）
     */
    @Transactional
    public Order createOrder(Long userId, OrderRequest req) {
        if (req.getCartItemIds() == null || req.getCartItemIds().isEmpty()) {
            throw new BusinessException("请选择要购买的商品");
        }
        List<CartItem> cartItems = cartItemRepository.findAllById(req.getCartItemIds());
        if (cartItems.isEmpty()) {
            throw new BusinessException("购物车商品不存在");
        }

        // 获取用户会员等级
        Integer memberLevel = getUserMemberLevel(userId);

        // 先计算订单总金额
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal originalTotal = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            if (!cartItem.getUserId().equals(userId)) {
                throw new BusinessException(403, "无权操作他人购物车");
            }
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new BusinessException("商品不存在"));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("商品【" + product.getName() + "】库存不足");
            }
            // 扣减库存
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // 计算会员折扣价格
            BigDecimal unitPrice = memberDiscountService.calculateDiscountPrice(product.getPrice(), memberLevel);
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            originalTotal = originalTotal.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // 先保存 Order 获取 ID
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStatus(0); // 已下单（待支付）
        order.setReceiver(req.getReceiver());
        order.setPhone(req.getPhone());
        order.setAddress(req.getAddress());
        order.setTotalAmount(total);
        order.setOriginalAmount(originalTotal);
        order.setDiscountAmount(originalTotal.subtract(total));
        order.setMemberLevel(memberLevel);
        Order savedOrder = orderRepository.save(order);

        // 再创建 OrderItem（此时 order 已有 ID）
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new BusinessException("商品不存在"));
            BigDecimal unitPrice = memberDiscountService.calculateDiscountPrice(product.getPrice(), memberLevel);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(unitPrice);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(savedOrder);
            orderItem.setOrderId(savedOrder.getId());
            orderItems.add(orderItem);
        }

        // 保存订单项
        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        // 清除已下单的购物车项
        cartItemRepository.deleteAll(cartItems);

        // 清除用户推荐缓存，下次加载时获取最新推荐
        recommendationService.evictUserCache(userId);

        return savedOrder;
    }

    private Integer getUserMemberLevel(Long userId) {
        if (userId == null) return 0;
        return userRepository.findById(userId)
                .map(User::getMemberLevel)
                .orElse(0);
    }

    /**
     * 模拟支付（状态0→1：已下单→已支付）
     */
    @Transactional
    public Order pay(Long orderId, Long userId) {
        Order order = ownOrder(orderId, userId);
        if (order.getStatus() != 0) {
            throw new BusinessException("当前状态不支持支付");
        }
        order.setStatus(1); // 已支付（待发货）
        Order saved = orderRepository.save(order);
        notificationService.notifyOrderStatus(saved, "已支付，等待发货");
        return saved;
    }

    /**
     * 管理员发货（状态1→2：已支付→已发货）
     */
    @Transactional
    public Order ship(Long orderId) {
        Order order = findOrder(orderId);
        if (order.getStatus() != 1) {
            throw new BusinessException("当前状态不支持发货");
        }
        order.setStatus(2); // 已发货（待收货）
        Order saved = orderRepository.save(order);
        notificationService.notifyOrderStatus(saved, "已发货，等待收货");
        return saved;
    }

    /**
     * 用户确认收货（状态2→3：已发货→已收货）
     */
    @Transactional
    public Order confirmReceive(Long orderId, Long userId) {
        Order order = ownOrder(orderId, userId);
        if (order.getStatus() != 2) {
            throw new BusinessException("当前状态不支持确认收货");
        }
        order.setStatus(3); // 已收货（待评价）
        return orderRepository.save(order);
    }

    /**
     * 用户评价完成后更新订单状态（状态3→4）
     */
    @Transactional
    public Order markReviewed(Long orderId) {
        Order order = findOrder(orderId);
        if (order.getStatus() == 3) {
            order.setStatus(4);
            order.setReviewed(1);
        }
        return orderRepository.save(order);
    }

    /**
     * 用户取消订单（仅状态0和1可取消）
     */
    @Transactional
    public Order cancel(Long orderId, Long userId, String reason) {
        Order order = ownOrder(orderId, userId);
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new BusinessException("当前状态不可取消订单（仅待支付和已支付状态可取消）");
        }
        // 恢复库存
        restoreStock(order);
        order.setPreviousStatus(order.getStatus());
        order.setStatus(-1);
        order.setCancelReason(reason != null ? reason : "用户主动取消");
        return orderRepository.save(order);
    }

    /**
     * 用户申请退单（状态2和3可申请）
     */
    @Transactional
    public Order requestReturn(Long orderId, Long userId, String reason) {
        Order order = ownOrder(orderId, userId);
        if (order.getStatus() != 2 && order.getStatus() != 3) {
            throw new BusinessException("当前状态不可申请退单（仅已发货和已收货状态可申请）");
        }
        order.setPreviousStatus(order.getStatus());
        order.setStatus(-2); // 申请退单
        order.setReturnReason(reason);
        return orderRepository.save(order);
    }

    /**
     * 管理员审核退单（-2→退单成功/驳回）
     */
    @Transactional
    public Order approveReturn(Long orderId, boolean approved) {
        Order order = findOrder(orderId);
        if (order.getStatus() != -2) {
            throw new BusinessException("当前状态不支持退单审核");
        }
        if (approved) {
            // 退单成功，恢复库存
            restoreStock(order);
            order.setStatus(-4); // 退单完成
        } else {
            // 驳回，恢复原状态
            Integer prev = order.getPreviousStatus();
            order.setStatus(prev != null ? prev : 2);
            order.setReturnReason(order.getReturnReason() + "(已驳回)");
        }
        Order saved = orderRepository.save(order);
        notificationService.notifyReturnResult(saved, approved);
        return saved;
    }

    /**
     * 管理员直接退单（仅已收货状态可操作）
     */
    @Transactional
    public Order adminReturn(Long orderId, String reason) {
        Order order = findOrder(orderId);
        if (order.getStatus() != 3) {
            throw new BusinessException("仅已收货状态的订单支持管理员直接退单");
        }
        restoreStock(order);
        order.setPreviousStatus(order.getStatus());
        order.setStatus(-4);
        order.setReturnReason(reason);
        return orderRepository.save(order);
    }

    /**
     * 用户订单列表
     */
    public List<Order> listByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        orders.forEach(o -> o.getItems().size());
        return orders;
    }

    public Order detail(Long id, Long userId) {
        Order order = findOrder(id);
        if (!order.getUserId().equals(userId) && !isAdmin(userId)) {
            throw new BusinessException(403, "无权查看该订单");
        }
        order.getItems().size();
        return order;
    }

    /**
     * 后台 - 所有订单
     */
    public List<Order> listAll() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(o -> o.getItems().size());
        return orders;
    }

    /**
     * 后台 - 更新订单状态（兼容旧接口）
     */
    @Transactional
    public Order updateStatus(Long orderId, String status) {
        Order order = findOrder(orderId);
        int newStatus = Integer.parseInt(status);
        order.setStatus(newStatus);
        // 如果是取消或退单成功，恢复库存
        if (newStatus == -1 || newStatus == -4) {
            restoreStock(order);
        }
        return orderRepository.save(order);
    }

    /**
     * 获取订单统计数据（管理端）
     */
    public java.util.Map<String, Object> getStats() {
        List<Order> all = orderRepository.findAll();
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("total", all.size());
        stats.put("pending", all.stream().filter(o -> o.getStatus() == 0).count());
        stats.put("paid", all.stream().filter(o -> o.getStatus() == 1).count());
        stats.put("shipped", all.stream().filter(o -> o.getStatus() == 2).count());
        stats.put("received", all.stream().filter(o -> o.getStatus() == 3).count());
        stats.put("reviewed", all.stream().filter(o -> o.getStatus() == 4).count());
        stats.put("cancelled", all.stream().filter(o -> o.getStatus() == -1).count());
        stats.put("returnRequested", all.stream().filter(o -> o.getStatus() == -2).count());
        stats.put("returned", all.stream().filter(o -> o.getStatus() == -4).count());

        // 总销售额
        BigDecimal totalAmount = all.stream()
                .filter(o -> o.getStatus() >= 1 && o.getStatus() <= 4)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalAmount", totalAmount);

        return stats;
    }

    // ---------- 私有辅助方法 ----------

    private Order ownOrder(Long orderId, Long userId) {
        Order order = findOrder(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作该订单");
        }
        return order;
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    private boolean isAdmin(Long userId) {
        return com.petshop.security.CurrentUser.isAdmin();
    }

    private String generateOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    private void restoreStock(Order order) {
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                try {
                    Product product = productRepository.findById(item.getProductId()).orElse(null);
                    if (product != null) {
                        product.setStock(product.getStock() + item.getQuantity());
                        productRepository.save(product);
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
