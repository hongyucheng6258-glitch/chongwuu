package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.dto.OrderRequest;
import com.petshop.entity.Order;
import com.petshop.security.CurrentUser;
import com.petshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 下单（状态0：已下单待支付） */
    @PostMapping
    public ApiResponse<Order> create(@Valid @RequestBody OrderRequest req) {
        return ApiResponse.success("下单成功", orderService.createOrder(CurrentUser.getUserId(), req));
    }

    /** 支付（状态0→1） */
    @PutMapping("/{id}/pay")
    public ApiResponse<Order> pay(@PathVariable Long id) {
        return ApiResponse.success("支付成功", orderService.pay(id, CurrentUser.getUserId()));
    }

    /** 确认收货（状态2→3） */
    @PutMapping("/{id}/receive")
    public ApiResponse<Order> confirmReceive(@PathVariable Long id) {
        return ApiResponse.success("已确认收货", orderService.confirmReceive(id, CurrentUser.getUserId()));
    }

    /** 用户取消订单（状态0或1→ -1） */
    @PutMapping("/{id}/cancel")
    public ApiResponse<Order> cancel(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : "用户主动取消";
        return ApiResponse.success("订单已取消", orderService.cancel(id, CurrentUser.getUserId(), reason));
    }

    /** 用户申请退单（状态2或3→ -2） */
    @PutMapping("/{id}/return")
    public ApiResponse<Order> requestReturn(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        return ApiResponse.success("退单申请已提交", orderService.requestReturn(id, CurrentUser.getUserId(), reason));
    }

    /** 用户订单列表 */
    @GetMapping
    public ApiResponse<List<Order>> list() {
        return ApiResponse.success(orderService.listByUser(CurrentUser.getUserId()));
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public ApiResponse<Order> detail(@PathVariable Long id) {
        return ApiResponse.success(orderService.detail(id, CurrentUser.getUserId()));
    }

    // ========== 后台管理 ==========

    /** 后台 - 所有订单 */
    @GetMapping("/admin")
    public ApiResponse<List<Order>> adminList() {
        checkAdmin();
        return ApiResponse.success(orderService.listAll());
    }

    /** 后台 - 发货（状态1→2） */
    @PutMapping("/admin/{id}/ship")
    public ApiResponse<Order> ship(@PathVariable Long id) {
        checkAdmin();
        return ApiResponse.success("已发货", orderService.ship(id));
    }

    /** 后台 - 退单审核（-2→ 成功转-4 或 恢复原状态） */
    @PutMapping("/admin/{id}/return-review")
    public ApiResponse<Order> returnReview(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        checkAdmin();
        boolean approved = body.getOrDefault("approved", false);
        return ApiResponse.success(approved ? "退单审核通过" : "退单已驳回", orderService.approveReturn(id, approved));
    }

    /** 后台 - 管理员直接退单（仅状态3可操作） */
    @PutMapping("/admin/{id}/direct-return")
    public ApiResponse<Order> directReturn(@PathVariable Long id, @RequestBody Map<String, String> body) {
        checkAdmin();
        String reason = body.get("reason");
        return ApiResponse.success("已退单", orderService.adminReturn(id, reason));
    }

    /** 后台 - 更新订单状态（兼容旧接口） */
    @PutMapping("/admin/{id}/status")
    public ApiResponse<Order> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        checkAdmin();
        String status = body.get("status");
        return ApiResponse.success("状态已更新", orderService.updateStatus(id, status));
    }

    /** 后台 - 订单统计 */
    @GetMapping("/admin/stats")
    public ApiResponse<Map<String, Object>> stats() {
        checkAdmin();
        return ApiResponse.success(orderService.getStats());
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
