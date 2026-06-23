package com.petshop.mongo.repository;

import com.petshop.mongo.entity.StoreProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB 商店商品关联 Repository
 */
@Repository
public interface StoreProductRepository extends MongoRepository<StoreProduct, String> {

    /**
     * 根据商店ID查找商品
     */
    List<StoreProduct> findByStoreId(Long storeId);

    /**
     * 根据商品ID查找所属商店
     */
    List<StoreProduct> findByProductId(Long productId);

    /**
     * 根据商店ID和商品类型查找
     */
    List<StoreProduct> findByStoreIdAndProductType(Long storeId, String productType);

    /**
     * 根据商品名称模糊搜索
     */
    List<StoreProduct> findByProductNameContainingIgnoreCase(String keyword);

    /**
     * 删除商店关联的所有商品
     */
    void deleteByStoreId(Long storeId);

    /**
     * 删除指定商品关联
     */
    void deleteByProductId(Long productId);
}
