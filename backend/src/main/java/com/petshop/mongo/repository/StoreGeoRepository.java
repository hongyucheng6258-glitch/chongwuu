package com.petshop.mongo.repository;

import com.petshop.mongo.entity.StoreGeo;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB 商店地理信息 Repository
 * 支持基于坐标的周边搜索
 */
@Repository
public interface StoreGeoRepository extends MongoRepository<StoreGeo, String> {

    /**
     * 根据 SQLite Store ID 查找
     */
    Optional<StoreGeo> findByStoreId(Long storeId);

    /**
     * 查找附近商店（基于 GeoJSON Point 和距离）
     */
    List<StoreGeo> findByLocationNear(Point point, Distance distance);

    /**
     * 根据状态查找
     */
    List<StoreGeo> findByStatus(Integer status);

    /**
     * 根据商店名称模糊搜索
     */
    List<StoreGeo> findByNameContainingIgnoreCase(String keyword);

    /**
     * 根据地址模糊搜索
     */
    List<StoreGeo> findByAddressContainingIgnoreCase(String keyword);

    /**
     * 删除关联的 SQLite Store 记录
     */
    void deleteByStoreId(Long storeId);
}
