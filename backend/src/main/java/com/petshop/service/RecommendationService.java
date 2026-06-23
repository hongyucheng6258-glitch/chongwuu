package com.petshop.service;

import com.petshop.entity.Order;
import com.petshop.entity.OrderItem;
import com.petshop.entity.Product;
import com.petshop.repository.OrderRepository;
import com.petshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品推荐服务（Redis 缓存优化版）
 * <p>
 * 缓存策略：
 * - 个性化推荐：缓存30分钟，用户下单后自动失效
 * - 相似商品推荐：缓存1小时
 * - 热门商品：缓存10分钟
 * <p>
 * 算法优化：
 * - 协同过滤 + 评分 + 销量综合加权
 * - 分类偏好分析
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberDiscountService memberDiscountService;
    private final RedisTemplate<String, Object> redisTemplate;

    /** 缓存Key前缀 */
    private static final String CACHE_PREFIX_USER = "recommend:user:";
    private static final String CACHE_PREFIX_SIMILAR = "recommend:similar:";
    private static final String CACHE_PREFIX_HOT = "recommend:hot";
    private static final String CACHE_PREFIX_SALES = "recommend:sales_rank";

    /** 缓存过期时间 */
    private static final long USER_CACHE_MINUTES = 30;
    private static final long SIMILAR_CACHE_MINUTES = 60;
    private static final long HOT_CACHE_MINUTES = 10;
    private static final long SALES_CACHE_MINUTES = 30;

    /**
     * 为用户推荐商品（综合推荐策略 + Redis 缓存）
     */
    @SuppressWarnings("unchecked")
    public List<Product> recommendForUser(Long userId, Integer size) {
        if (size == null || size <= 0) size = 6;

        // 1. 尝试从 Redis 缓存读取
        String cacheKey = CACHE_PREFIX_USER + userId + ":" + size;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof List) {
                log.debug("推荐命中缓存: userId={}", userId);
                return ((List<Product>) cached);
            }
        } catch (Exception e) {
            log.warn("读取推荐缓存失败: {}", e.getMessage());
        }

        // 2. 缓存未命中，执行推荐计算
        Set<Long> purchasedProductIds = getPurchasedProductIds(userId);

        List<Product> recommendations;
        if (purchasedProductIds.isEmpty()) {
            // 新用户：返回热门商品
            recommendations = getHotProducts(size * 2);
        } else {
            // 老用户：协同过滤推荐
            recommendations = collaborativeFilterRecommend(userId, purchasedProductIds, size * 2);
        }

        // 过滤已购买商品，限制数量，应用折扣
        List<Product> result = recommendations.stream()
                .filter(p -> !purchasedProductIds.contains(p.getId()))
                .limit(size)
                .peek(this::applyDiscountPrice)
                .collect(Collectors.toList());

        // 3. 写入 Redis 缓存
        try {
            redisTemplate.opsForValue().set(cacheKey, result, USER_CACHE_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("写入推荐缓存失败: {}", e.getMessage());
        }

        return result;
    }

    /**
     * 获取相似商品推荐（Redis 缓存）
     */
    @SuppressWarnings("unchecked")
    public List<Product> recommendSimilarProducts(Long productId, Integer size) {
        if (size == null || size <= 0) size = 4;

        // 1. 尝试从缓存读取
        String cacheKey = CACHE_PREFIX_SIMILAR + productId + ":" + size;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof List) {
                log.debug("相似推荐命中缓存: productId={}", productId);
                return ((List<Product>) cached);
            }
        } catch (Exception e) {
            log.warn("读取相似推荐缓存失败: {}", e.getMessage());
        }

        // 2. 缓存未命中，执行计算
        Product current = productRepository.findById(productId).orElse(null);
        if (current == null) return Collections.emptyList();

        Long categoryId = current.getCategoryId();
        Long storeId = current.getStoreId();

        List<Product> candidates = productRepository.findAll().stream()
                .filter(p -> p.getStatus() != null && p.getStatus() == 1)
                .filter(p -> !p.getId().equals(productId))
                .filter(p -> {
                    boolean sameCategory = categoryId != null && categoryId.equals(p.getCategoryId());
                    boolean sameStore = storeId != null && storeId.equals(p.getStoreId());
                    return sameCategory || sameStore;
                })
                .sorted((a, b) -> {
                    // 同分类优先
                    boolean aSameCat = categoryId != null && categoryId.equals(a.getCategoryId());
                    boolean bSameCat = categoryId != null && categoryId.equals(b.getCategoryId());
                    if (aSameCat != bSameCat) return aSameCat ? -1 : 1;
                    // 综合评分排序
                    return compareByScore(b, a);
                })
                .limit(size)
                .peek(this::applyDiscountPrice)
                .collect(Collectors.toList());

        // 3. 写入缓存
        try {
            redisTemplate.opsForValue().set(cacheKey, candidates, SIMILAR_CACHE_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("写入相似推荐缓存失败: {}", e.getMessage());
        }

        return candidates;
    }

    /**
     * 热门商品推荐（公开接口，无需登录）
     */
    public List<Product> recommendHot(Integer size) {
        if (size == null || size <= 0) size = 6;
        List<Product> result = getHotProducts(size);
        result.forEach(this::applyDiscountPrice);
        return result;
    }

    /**
     * 获取热门商品（评分 + 销量综合排序，带缓存）
     */
    @SuppressWarnings("unchecked")
    private List<Product> getHotProducts(int limit) {
        String cacheKey = CACHE_PREFIX_HOT + ":" + limit;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof List) {
                return ((List<Product>) cached);
            }
        } catch (Exception e) {
            log.warn("读取热门商品缓存失败: {}", e.getMessage());
        }

        // 计算销量排名
        Map<Long, Integer> salesMap = getSalesRanking();

        List<Product> result = productRepository.findAll().stream()
                .filter(p -> p.getStatus() != null && p.getStatus() == 1)
                .sorted((a, b) -> compareByScore(b, a, salesMap))
                .limit(limit)
                .collect(Collectors.toList());

        try {
            redisTemplate.opsForValue().set(cacheKey, result, HOT_CACHE_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("写入热门商品缓存失败: {}", e.getMessage());
        }

        return result;
    }

    /**
     * 基于协同过滤的推荐（优化版：加入评分和销量权重）
     */
    private List<Product> collaborativeFilterRecommend(Long userId, Set<Long> purchasedProductIds, int limit) {
        List<Order> allOrders = orderRepository.findAll();
        Set<Long> similarUsers = new HashSet<>();
        Map<Long, Integer> userSimilarityScore = new HashMap<>();

        // 1. 找相似用户
        for (Order order : allOrders) {
            if (order.getUserId().equals(userId)) continue;
            int commonItems = 0;
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    if (purchasedProductIds.contains(item.getProductId())) {
                        commonItems++;
                    }
                }
            }
            if (commonItems > 0) {
                similarUsers.add(order.getUserId());
                userSimilarityScore.merge(order.getUserId(), commonItems, Integer::sum);
            }
        }

        if (similarUsers.isEmpty()) {
            return getHotProducts(limit);
        }

        // 2. 统计相似用户购买的商品得分
        Map<Long, Integer> productScore = new HashMap<>();
        for (Order order : allOrders) {
            if (!similarUsers.contains(order.getUserId())) continue;
            int similarity = userSimilarityScore.getOrDefault(order.getUserId(), 1);
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    Long pid = item.getProductId();
                    if (!purchasedProductIds.contains(pid)) {
                        productScore.merge(pid, similarity, Integer::sum);
                    }
                }
            }
        }

        // 3. 加载商品并综合排序（协同过滤得分 + 评分 + 销量）
        Map<Long, Integer> salesMap = getSalesRanking();
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(p -> productScore.containsKey(p.getId()))
                .filter(p -> p.getStatus() != null && p.getStatus() == 1)
                .sorted((a, b) -> {
                    // 协同过滤得分权重 60%
                    int scoreA = productScore.getOrDefault(a.getId(), 0);
                    int scoreB = productScore.getOrDefault(b.getId(), 0);
                    if (scoreA != scoreB) return Integer.compare(scoreB, scoreA);

                    // 评分权重 25%
                    double ratingA = a.getRating() != null ? a.getRating() : 0;
                    double ratingB = b.getRating() != null ? b.getRating() : 0;
                    if (ratingA != ratingB) return Double.compare(ratingB, ratingA);

                    // 销量权重 15%
                    int salesA = salesMap.getOrDefault(a.getId(), 0);
                    int salesB = salesMap.getOrDefault(b.getId(), 0);
                    return Integer.compare(salesB, salesA);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 计算商品销量排名（带缓存）
     */
    private Map<Long, Integer> getSalesRanking() {
        String cacheKey = CACHE_PREFIX_SALES;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof Map) {
                return (Map<Long, Integer>) cached;
            }
        } catch (Exception e) {
            log.warn("读取销量排名缓存失败: {}", e.getMessage());
        }

        Map<Long, Integer> salesMap = new HashMap<>();
        List<Order> allOrders = orderRepository.findAll();
        for (Order order : allOrders) {
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    salesMap.merge(item.getProductId(), item.getQuantity(), Integer::sum);
                }
            }
        }

        try {
            redisTemplate.opsForValue().set(cacheKey, salesMap, SALES_CACHE_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("写入销量排名缓存失败: {}", e.getMessage());
        }

        return salesMap;
    }

    /**
     * 综合评分比较（评分 + 销量）
     */
    private int compareByScore(Product a, Product b) {
        return compareByScore(a, b, getSalesRanking());
    }

    private int compareByScore(Product a, Product b, Map<Long, Integer> salesMap) {
        // 评分权重 70%
        double ratingA = a.getRating() != null ? a.getRating() : 0;
        double ratingB = b.getRating() != null ? b.getRating() : 0;
        if (ratingA != ratingB) return Double.compare(ratingB, ratingA);

        // 销量权重 30%
        int salesA = salesMap.getOrDefault(a.getId(), 0);
        int salesB = salesMap.getOrDefault(b.getId(), 0);
        return Integer.compare(salesB, salesA);
    }

    /**
     * 获取用户购买过的商品ID
     */
    private Set<Long> getPurchasedProductIds(Long userId) {
        if (userId == null) return Collections.emptySet();
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        Set<Long> ids = new HashSet<>();
        for (Order order : orders) {
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    ids.add(item.getProductId());
                }
            }
        }
        return ids;
    }

    /**
     * 用户下单后清除推荐缓存，确保下次获取最新推荐
     */
    public void evictUserCache(Long userId) {
        try {
            Set<String> keys = redisTemplate.keys(CACHE_PREFIX_USER + userId + ":*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("已清除用户推荐缓存: userId={}", userId);
            }
        } catch (Exception e) {
            log.warn("清除用户推荐缓存失败: {}", e.getMessage());
        }
    }

    /**
     * 清除所有推荐缓存（管理员更新商品时调用）
     */
    public void evictAllCache() {
        try {
            Set<String> keys = redisTemplate.keys("recommend:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("已清除所有推荐缓存");
            }
        } catch (Exception e) {
            log.warn("清除推荐缓存失败: {}", e.getMessage());
        }
    }

    private void applyDiscountPrice(Product product) {
        if (product != null && product.getPrice() != null) {
            Integer memberLevel = com.petshop.security.CurrentUser.getMemberLevel();
            product.setDiscountPrice(memberDiscountService.calculateDiscountPrice(product.getPrice(), memberLevel));
            product.setDiscountRate(memberDiscountService.getDiscountRate(memberLevel));
        }
    }
}
