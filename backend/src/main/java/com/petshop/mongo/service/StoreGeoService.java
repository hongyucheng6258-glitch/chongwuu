package com.petshop.mongo.service;

import com.petshop.entity.Store;
import com.petshop.mongo.entity.StoreGeo;
import com.petshop.mongo.repository.StoreGeoRepository;
import com.petshop.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MongoDB 商店地理信息服务
 * 实现周边搜索、坐标同步、导航链接生成
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreGeoService {

    private final StoreGeoRepository storeGeoRepository;
    private final StoreRepository storeRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 同步 SQLite Store 到 MongoDB（初始化或更新）
     */
    public StoreGeo syncStore(Store store) {
        if (store == null || store.getLatitude() == null || store.getLongitude() == null) {
            return null;
        }
        StoreGeo geo = storeGeoRepository.findByStoreId(store.getId()).orElse(new StoreGeo());
        geo.setStoreId(store.getId());
        geo.setName(store.getName());
        geo.setAddress(store.getAddress());
        geo.setPhone(store.getPhone());
        geo.setLocation(store.getLatitude(), store.getLongitude());
        geo.setDescription(store.getDescription());
        geo.setImage(store.getImage());
        geo.setBusinessHours(store.getBusinessHours());
        geo.setStatus(store.getStatus());
        if (geo.getCreatedAt() == null) {
            geo.setCreatedAt(LocalDateTime.now());
        }
        geo.setUpdatedAt(LocalDateTime.now());
        return storeGeoRepository.save(geo);
    }

    /**
     * 同步所有商店到 MongoDB
     */
    public int syncAllStores() {
        List<Store> stores = storeRepository.findAll();
        int count = 0;
        for (Store store : stores) {
            if (store.getLatitude() != null && store.getLongitude() != null) {
                syncStore(store);
                count++;
            }
        }
        log.info("同步 {} 个商店到 MongoDB", count);
        return count;
    }

    /**
     * 查找附近商店（基于 MongoDB $geoWithin $centerSphere 查询）
     */
    public List<StoreGeo> findNearby(BigDecimal lat, BigDecimal lng, double radiusKm) {
        // 使用 $geoWithin + $centerSphere 查询，比 $near 更可靠
        // centerSphere: [ [lng, lat], radiusInRadians ]
        double radiusRadians = radiusKm / 6371.0;
        org.springframework.data.mongodb.core.query.BasicQuery query =
                new org.springframework.data.mongodb.core.query.BasicQuery(
                        "{ location: { $geoWithin: { $centerSphere: [ [" + lng.doubleValue()
                                + ", " + lat.doubleValue() + "], " + radiusRadians + " ] } } }"
                );
        return mongoTemplate.find(query, StoreGeo.class);
    }

    /**
     * 查找附近商店并按距离排序（带距离信息）
     */
    public List<StoreGeoWithDistance> findNearbyWithDistance(BigDecimal lat, BigDecimal lng, double radiusKm) {
        List<StoreGeo> stores = findNearby(lat, lng, radiusKm);
        return stores.stream()
                .map(s -> {
                    double dist = calcDistance(lat, lng, s.getLatitude(), s.getLongitude());
                    return new StoreGeoWithDistance(s, dist);
                })
                .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .collect(Collectors.toList());
    }

    /**
     * 搜索商店（名称或地址）
     */
    public List<StoreGeo> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return storeGeoRepository.findByStatus(1);
        }
        String k = keyword.trim();
        List<StoreGeo> byName = storeGeoRepository.findByNameContainingIgnoreCase(k);
        List<StoreGeo> byAddr = storeGeoRepository.findByAddressContainingIgnoreCase(k);
        // 合并去重
        List<StoreGeo> result = new ArrayList<>(byName);
        for (StoreGeo s : byAddr) {
            if (result.stream().noneMatch(r -> r.getStoreId().equals(s.getStoreId()))) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * 根据 Store ID 查找
     */
    public Optional<StoreGeo> findByStoreId(Long storeId) {
        return storeGeoRepository.findByStoreId(storeId);
    }

    /**
     * 删除 MongoDB 中的商店记录
     */
    public void deleteByStoreId(Long storeId) {
        storeGeoRepository.deleteByStoreId(storeId);
    }

    /**
     * 生成高德地图导航链接
     */
    public String generateAmapNavigationUrl(BigDecimal fromLat, BigDecimal fromLng,
                                           BigDecimal toLat, BigDecimal toLng, String toName) {
        String name = toName != null ? java.net.URLEncoder.encode(toName) : "";
        return String.format("https://uri.amap.com/navigation?from=%s,%s&to=%s,%s,%s&mode=car&callnative=1",
                fromLng, fromLat, toLng, toLat, name);
    }

    /**
     * 生成百度地图导航链接
     */
    public String generateBaiduNavigationUrl(BigDecimal fromLat, BigDecimal fromLng,
                                            BigDecimal toLat, BigDecimal toLng, String toName) {
        String name = toName != null ? java.net.URLEncoder.encode(toName) : "";
        return String.format("https://api.map.baidu.com/direction?origin=%s,%s&destination=%s,%s&mode=driving&src=webapp.petshop",
                fromLat, fromLng, toLat, toLng);
    }

    private double calcDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) return Double.MAX_VALUE;
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLng = Math.toRadians(lng2.doubleValue() - lng1.doubleValue());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /**
     * 带距离信息的商店包装类
     */
    public static class StoreGeoWithDistance {
        private final StoreGeo store;
        private final double distanceKm;

        public StoreGeoWithDistance(StoreGeo store, double distanceKm) {
            this.store = store;
            this.distanceKm = distanceKm;
        }

        public StoreGeo getStore() { return store; }
        public double getDistanceKm() { return distanceKm; }
        public String getDistanceText() {
            if (distanceKm < 1) {
                return String.format("%.0f米", distanceKm * 1000);
            } else {
                return String.format("%.1f公里", distanceKm);
            }
        }
    }
}
