package com.petshop.mongo.config;

import com.petshop.mongo.entity.StoreGeo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

/**
 * MongoDB 索引配置
 * 确保地理空间索引正确创建
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        try {
            // 创建 2dsphere 地理空间索引
            mongoTemplate.indexOps(StoreGeo.class)
                    .ensureIndex(new GeospatialIndex("location"));
            log.info("MongoDB 地理空间索引创建成功: store_geo.location");
        } catch (Exception e) {
            log.warn("MongoDB 索引创建失败（可能已存在）: {}", e.getMessage());
        }
    }
}
