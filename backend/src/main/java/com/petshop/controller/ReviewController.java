package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.dto.ReviewRequest;
import com.petshop.entity.Review;
import com.petshop.security.CurrentUser;
import com.petshop.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /** 前台 - 发表评价 */
    @PostMapping
    public ApiResponse<Review> create(@Valid @RequestBody ReviewRequest req) {
        return ApiResponse.success("评价成功", reviewService.create(req));
    }

    /** 前台 - 按商品查评价 */
    @GetMapping("/product/{productId}")
    public ApiResponse<List<Review>> byProduct(@PathVariable Long productId) {
        return ApiResponse.success(reviewService.listByProduct(productId));
    }

    /** 前台 - 我的评价 */
    @GetMapping("/my")
    public ApiResponse<List<Review>> myReviews() {
        return ApiResponse.success(reviewService.listByUser());
    }

    /** 删除评价 */
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ApiResponse.success("评价已删除", null);
    }

    /** 后台 - 全部评价 */
    @GetMapping("/admin")
    public ApiResponse<List<Review>> adminList() {
        checkAdmin();
        return ApiResponse.success(reviewService.listAll());
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
