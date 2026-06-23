package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.StoreRequest;
import com.petshop.entity.Store;
import com.petshop.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<Store> listActive() {
        return storeRepository.findByStatus(1);
    }

    public List<Store> listAll() {
        return storeRepository.findAll();
    }

    public Store detail(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("商店不存在"));
    }

    /**
     * 搜索附近商店（简化：按名称搜索）
     */
    public List<Store> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return storeRepository.findByStatus(1);
        }
        return storeRepository.search(keyword.trim());
    }

    /**
     * 按距离排序（简化实现，基于坐标计算附近商店）
     */
    public List<Store> nearby(BigDecimal latitude, BigDecimal longitude, double radiusKm) {
        List<Store> stores = storeRepository.findByStatus(1);
        stores.sort((a, b) -> {
            double distA = calcDistance(latitude, longitude, a.getLatitude(), a.getLongitude());
            double distB = calcDistance(latitude, longitude, b.getLatitude(), b.getLongitude());
            return Double.compare(distA, distB);
        });
        // 过滤半径范围
        stores.removeIf(s -> {
            double d = calcDistance(latitude, longitude, s.getLatitude(), s.getLongitude());
            return d > radiusKm;
        });
        return stores;
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

    @Transactional
    public Store create(StoreRequest req) {
        if (storeRepository.existsByName(req.getName())) {
            throw new BusinessException("商店名称已存在");
        }
        Store store = new Store();
        applyRequest(store, req);
        return storeRepository.save(store);
    }

    @Transactional
    public Store update(Long id, StoreRequest req) {
        Store store = detail(id);
        applyRequest(store, req);
        return storeRepository.save(store);
    }

    @Transactional
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

    private void applyRequest(Store store, StoreRequest req) {
        if (req.getName() != null) store.setName(req.getName());
        if (req.getAddress() != null) store.setAddress(req.getAddress());
        if (req.getPhone() != null) store.setPhone(req.getPhone());
        if (req.getLatitude() != null) store.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) store.setLongitude(req.getLongitude());
        if (req.getDescription() != null) store.setDescription(req.getDescription());
        if (req.getImage() != null) store.setImage(req.getImage());
        if (req.getBusinessHours() != null) store.setBusinessHours(req.getBusinessHours());
        if (req.getStatus() != null) store.setStatus(req.getStatus());
    }
}
