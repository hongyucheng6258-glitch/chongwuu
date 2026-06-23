package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.dto.ProductRequest;
import com.petshop.entity.Product;
import com.petshop.security.CurrentUser;
import com.petshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 前台商品列表 (公开)
     */
    @GetMapping("/list")
    public ApiResponse<Page<Product>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ApiResponse.success(productService.listForUser(categoryId, keyword, page, size));
    }

    /**
     * 商品详情 (公开)
     */
    @GetMapping("/detail/{id}")
    public ApiResponse<Product> detail(@PathVariable Long id) {
        return ApiResponse.success(productService.detail(id));
    }

    /**
     * 搜索 (公开)
     */
    @GetMapping("/search")
    public ApiResponse<Page<Product>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ApiResponse.success(productService.listForUser(null, keyword, page, size));
    }

    /**
     * 后台商品列表
     */
    @GetMapping("/admin")
    public ApiResponse<Page<Product>> adminList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        checkAdmin();
        return ApiResponse.success(productService.listForAdmin(page, size));
    }

    @PostMapping
    public ApiResponse<Product> create(@RequestBody ProductRequest req) {
        checkAdmin();
        return ApiResponse.success("创建成功", productService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable Long id, @RequestBody ProductRequest req) {
        checkAdmin();
        return ApiResponse.success("更新成功", productService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        checkAdmin();
        productService.delete(id);
        return ApiResponse.success("删除成功", null);
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
