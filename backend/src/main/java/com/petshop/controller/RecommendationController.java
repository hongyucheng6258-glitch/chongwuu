package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.entity.Product;
import com.petshop.security.CurrentUser;
import com.petshop.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品推荐接口
 */
@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * 为用户推荐商品（需登录）
     * 基于协同过滤算法，推荐用户可能感兴趣的商品
     */
    @GetMapping("/for-me")
    public ApiResponse<List<Product>> recommendForMe(
            @RequestParam(defaultValue = "6") Integer size) {
        Long userId = CurrentUser.getUserId();
        return ApiResponse.success(recommendationService.recommendForUser(userId, size));
    }

    /**
     * 热门商品推荐（公开，无需登录）
     * 基于评分和销量综合排序
     */
    @GetMapping("/hot")
    public ApiResponse<List<Product>> recommendHot(
            @RequestParam(defaultValue = "6") Integer size) {
        return ApiResponse.success(recommendationService.recommendHot(size));
    }

    /**
     * 相似商品推荐（公开）
     * 基于商品关联度推荐相似商品
     */
    @GetMapping("/similar/{productId}")
    public ApiResponse<List<Product>> recommendSimilar(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "4") Integer size) {
        return ApiResponse.success(recommendationService.recommendSimilarProducts(productId, size));
    }
}
