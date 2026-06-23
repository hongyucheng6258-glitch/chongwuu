package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.dto.CartRequest;
import com.petshop.entity.CartItem;
import com.petshop.security.CurrentUser;
import com.petshop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ApiResponse<List<CartItem>> list() {
        return ApiResponse.success(cartService.list(CurrentUser.getUserId()));
    }

    @PostMapping
    public ApiResponse<CartItem> add(@Valid @RequestBody CartRequest req) {
        return ApiResponse.success("已加入购物车", cartService.add(CurrentUser.getUserId(), req));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        CartItem item = cartService.updateQuantity(CurrentUser.getUserId(), id, quantity);
        return ApiResponse.success(item == null ? "已移除" : "数量已更新", item);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> remove(@PathVariable Long id) {
        cartService.remove(CurrentUser.getUserId(), id);
        return ApiResponse.success("已移除", null);
    }

    @DeleteMapping
    public ApiResponse<?> clear() {
        cartService.clear(CurrentUser.getUserId());
        return ApiResponse.success("购物车已清空", null);
    }
}
