package com.petshop.service;

import com.petshop.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员等级折扣服务
 * 0-普通用户: 无折扣
 * 1-银卡会员: 95折
 * 2-金卡会员: 88折
 * 3-钻石会员: 8折
 */
@Service
public class MemberDiscountService {

    private static final Map<Integer, BigDecimal> DISCOUNT_MAP = new HashMap<>();
    private static final Map<Integer, String> LEVEL_NAME_MAP = new HashMap<>();

    static {
        DISCOUNT_MAP.put(0, new BigDecimal("1.00"));   // 普通用户: 原价
        DISCOUNT_MAP.put(1, new BigDecimal("0.95"));   // 银卡: 95折
        DISCOUNT_MAP.put(2, new BigDecimal("0.88"));   // 金卡: 88折
        DISCOUNT_MAP.put(3, new BigDecimal("0.80"));   // 钻石: 8折

        LEVEL_NAME_MAP.put(0, "普通用户");
        LEVEL_NAME_MAP.put(1, "银卡会员");
        LEVEL_NAME_MAP.put(2, "金卡会员");
        LEVEL_NAME_MAP.put(3, "钻石会员");
    }

    /**
     * 获取会员折扣率
     */
    public BigDecimal getDiscountRate(Integer memberLevel) {
        return DISCOUNT_MAP.getOrDefault(memberLevel != null ? memberLevel : 0, BigDecimal.ONE);
    }

    /**
     * 获取会员等级名称
     */
    public String getLevelName(Integer memberLevel) {
        return LEVEL_NAME_MAP.getOrDefault(memberLevel != null ? memberLevel : 0, "普通用户");
    }

    /**
     * 计算折扣后价格
     */
    public BigDecimal calculateDiscountPrice(BigDecimal originalPrice, Integer memberLevel) {
        if (originalPrice == null) return BigDecimal.ZERO;
        BigDecimal rate = getDiscountRate(memberLevel);
        return originalPrice.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算优惠金额
     */
    public BigDecimal calculateDiscountAmount(BigDecimal originalPrice, Integer memberLevel) {
        if (originalPrice == null) return BigDecimal.ZERO;
        return originalPrice.subtract(calculateDiscountPrice(originalPrice, memberLevel));
    }

    /**
     * 获取所有等级配置
     */
    public Map<String, Object> getAllLevelConfigs() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<Integer, BigDecimal> entry : DISCOUNT_MAP.entrySet()) {
            Map<String, Object> config = new HashMap<>();
            config.put("level", entry.getKey());
            config.put("name", LEVEL_NAME_MAP.get(entry.getKey()));
            config.put("discountRate", entry.getValue());
            config.put("discountPercent", entry.getValue().multiply(new BigDecimal("100")).intValue());
            result.put("level" + entry.getKey(), config);
        }
        return result;
    }
}
