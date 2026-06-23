package com.petshop.mongo.controller;

import com.petshop.common.ApiResponse;
import com.petshop.mongo.entity.StoreGeo;
import com.petshop.mongo.service.StoreGeoService;
import com.petshop.mongo.service.StoreGeoService.StoreGeoWithDistance;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB 商店地理信息 API
 * 提供周边搜索、地图导航、商店同步等功能
 */
@RestController
@RequestMapping("/api/stores/geo")
@RequiredArgsConstructor
public class StoreGeoController {

    private final StoreGeoService storeGeoService;
    private final MongoTemplate mongoTemplate;

    /**
     * 同步所有 SQLite 商店到 MongoDB
     */
    @PostMapping("/sync")
    public ApiResponse<Map<String, Object>> syncAll() {
        int count = storeGeoService.syncAllStores();
        Map<String, Object> result = new HashMap<>();
        result.put("syncedCount", count);
        return ApiResponse.success("同步完成", result);
    }

    /**
     * 周边搜索 - 基于 MongoDB 地理空间查询
     */
    @GetMapping("/nearby")
    public ApiResponse<List<StoreGeoWithDistance>> nearby(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng,
            @RequestParam(defaultValue = "10") double radius) {
        return ApiResponse.success(storeGeoService.findNearbyWithDistance(lat, lng, radius));
    }

    /**
     * 搜索商店（名称或地址）
     */
    @GetMapping("/search")
    public ApiResponse<List<StoreGeo>> search(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(storeGeoService.search(keyword));
    }

    /**
     * 获取导航链接
     */
    @GetMapping("/navigate")
    public ApiResponse<Map<String, String>> navigate(
            @RequestParam BigDecimal fromLat,
            @RequestParam BigDecimal fromLng,
            @RequestParam BigDecimal toLat,
            @RequestParam BigDecimal toLng,
            @RequestParam(required = false) String toName,
            @RequestParam(defaultValue = "amap") String type) {
        String url;
        if ("baidu".equalsIgnoreCase(type)) {
            url = storeGeoService.generateBaiduNavigationUrl(fromLat, fromLng, toLat, toLng, toName);
        } else {
            url = storeGeoService.generateAmapNavigationUrl(fromLat, fromLng, toLat, toLng, toName);
        }
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        result.put("type", type);
        return ApiResponse.success(result);
    }

    /**
     * 根据 SQLite Store ID 获取 MongoDB 地理信息
     */
    @GetMapping("/{storeId}")
    public ApiResponse<StoreGeo> getByStoreId(@PathVariable Long storeId) {
        return storeGeoService.findByStoreId(storeId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "商店地理信息不存在"));
    }

    /**
     * 调试接口：查看 MongoDB 中所有商店地理数据
     */
    @GetMapping("/debug/list")
    public ApiResponse<List<StoreGeo>> debugList() {
        List<StoreGeo> all = mongoTemplate.findAll(StoreGeo.class);
        return ApiResponse.success(all);
    }
}
