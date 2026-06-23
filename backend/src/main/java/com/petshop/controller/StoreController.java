package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.dto.StoreRequest;
import com.petshop.entity.Store;
import com.petshop.security.CurrentUser;
import com.petshop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /** 前台 - 获取营业中的商店 */
    @GetMapping("/list")
    public ApiResponse<List<Store>> list() {
        return ApiResponse.success(storeService.listActive());
    }

    /** 前台 - 搜索商店 */
    @GetMapping("/search")
    public ApiResponse<List<Store>> search(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(storeService.search(keyword));
    }

    /** 前台 - 附近商店 */
    @GetMapping("/nearby")
    public ApiResponse<List<Store>> nearby(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng,
            @RequestParam(defaultValue = "10") double radius) {
        return ApiResponse.success(storeService.nearby(lat, lng, radius));
    }

    /** 商店详情 */
    @GetMapping("/detail/{id}")
    public ApiResponse<Store> detail(@PathVariable Long id) {
        return ApiResponse.success(storeService.detail(id));
    }

    /** 后台 - 全部商店 */
    @GetMapping("/admin")
    public ApiResponse<List<Store>> adminList() {
        checkAdmin();
        return ApiResponse.success(storeService.listAll());
    }

    @PostMapping
    public ApiResponse<Store> create(@RequestBody StoreRequest req) {
        checkAdmin();
        return ApiResponse.success("商店创建成功", storeService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Store> update(@PathVariable Long id, @RequestBody StoreRequest req) {
        checkAdmin();
        return ApiResponse.success("商店更新成功", storeService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        checkAdmin();
        storeService.delete(id);
        return ApiResponse.success("商店已删除", null);
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
