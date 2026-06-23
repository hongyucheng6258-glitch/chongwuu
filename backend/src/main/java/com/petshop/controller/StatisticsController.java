package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.security.CurrentUser;
import com.petshop.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 统计数据接口
 */
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /** 仪表盘完整统计数据 */
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getDashboardStats());
    }

    /** 概览数据 */
    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getOverviewStats());
    }

    /** 销售趋势 */
    @GetMapping("/sales-trend")
    public ApiResponse<List<Map<String, Object>>> salesTrend() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getSalesTrend(7));
    }

    /** 分类销售分布 */
    @GetMapping("/category-distribution")
    public ApiResponse<List<Map<String, Object>>> categoryDistribution() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getCategoryDistribution());
    }

    /** 热销商品排行 */
    @GetMapping("/top-products")
    public ApiResponse<List<Map<String, Object>>> topProducts() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getTopSellingProducts(5));
    }

    /** 用户增长趋势 */
    @GetMapping("/user-growth")
    public ApiResponse<List<Map<String, Object>>> userGrowth() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getUserGrowth(7));
    }

    /** 会员等级分布 */
    @GetMapping("/member-distribution")
    public ApiResponse<List<Map<String, Object>>> memberDistribution() {
        checkAdmin();
        return ApiResponse.success(statisticsService.getMemberDistribution());
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
