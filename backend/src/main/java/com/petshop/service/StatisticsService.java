package com.petshop.service;

import com.petshop.entity.Order;
import com.petshop.entity.Product;
import com.petshop.entity.User;
import com.petshop.repository.OrderRepository;
import com.petshop.repository.ProductRepository;
import com.petshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计数据服务
 * 提供多维度平台统计数据
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * 获取首页仪表盘完整统计数据
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("overview", getOverviewStats());
        stats.put("salesTrend", getSalesTrend(7));
        stats.put("categoryDistribution", getCategoryDistribution());
        stats.put("topProducts", getTopSellingProducts(5));
        stats.put("userGrowth", getUserGrowth(7));
        stats.put("memberDistribution", getMemberDistribution());
        return stats;
    }

    /**
     * 概览数据
     */
    public Map<String, Object> getOverviewStats() {
        List<Order> allOrders = orderRepository.findAll();
        List<Product> allProducts = productRepository.findAll();
        List<User> allUsers = userRepository.findAll();

        Map<String, Object> overview = new HashMap<>();

        // 订单统计
        overview.put("totalOrders", allOrders.size());
        overview.put("pendingOrders", countByStatus(allOrders, 0));
        overview.put("paidOrders", countByStatus(allOrders, 1));
        overview.put("shippedOrders", countByStatus(allOrders, 2));
        overview.put("completedOrders", countByStatus(allOrders, 3) + countByStatus(allOrders, 4));
        overview.put("cancelledOrders", countByStatus(allOrders, -1));
        overview.put("returnedOrders", countByStatus(allOrders, -4));

        // 销售额（仅统计已支付及以上状态的订单）
        BigDecimal totalSales = allOrders.stream()
                .filter(o -> o.getStatus() >= 1 && o.getStatus() <= 4)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        overview.put("totalSales", totalSales);

        // 今日销售额
        LocalDate today = LocalDate.now();
        BigDecimal todaySales = allOrders.stream()
                .filter(o -> o.getStatus() >= 1 && o.getStatus() <= 4)
                .filter(o -> o.getCreatedAt() != null && o.getCreatedAt().toLocalDate().equals(today))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        overview.put("todaySales", todaySales);

        // 商品统计
        overview.put("totalProducts", allProducts.size());
        overview.put("onSaleProducts", allProducts.stream().filter(p -> p.getStatus() != null && p.getStatus() == 1).count());

        // 用户统计
        overview.put("totalUsers", allUsers.size());
        overview.put("newUsersToday", allUsers.stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().toLocalDate().equals(today))
                .count());

        return overview;
    }

    /**
     * 销售趋势（最近N天）
     */
    public List<Map<String, Object>> getSalesTrend(int days) {
        List<Order> allOrders = orderRepository.findAll();
        List<Map<String, Object>> trend = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            BigDecimal sales = allOrders.stream()
                    .filter(o -> o.getStatus() >= 1 && o.getStatus() <= 4)
                    .filter(o -> o.getCreatedAt() != null && o.getCreatedAt().toLocalDate().equals(date))
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long orderCount = allOrders.stream()
                    .filter(o -> o.getCreatedAt() != null && o.getCreatedAt().toLocalDate().equals(date))
                    .count();

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date.format(formatter));
            dayData.put("sales", sales);
            dayData.put("orderCount", orderCount);
            trend.add(dayData);
        }
        return trend;
    }

    /**
     * 商品分类销售分布
     */
    public List<Map<String, Object>> getCategoryDistribution() {
        List<Order> allOrders = orderRepository.findAll();
        List<Product> allProducts = productRepository.findAll();

        // 统计各分类的销售额
        Map<Long, BigDecimal> categorySales = new HashMap<>();
        Map<Long, String> categoryNames = new HashMap<>();

        for (Order order : allOrders) {
            if (order.getStatus() < 0) continue; // 跳过取消/退单
            if (order.getItems() != null) {
                for (var item : order.getItems()) {
                    Product product = allProducts.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst().orElse(null);
                    if (product != null && product.getCategoryId() != null) {
                        Long catId = product.getCategoryId();
                        BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        categorySales.merge(catId, itemTotal, BigDecimal::add);
                    }
                }
            }
        }

        // 获取分类名称
        for (Product p : allProducts) {
            if (p.getCategoryId() != null && p.getCategory() != null) {
                categoryNames.put(p.getCategoryId(), p.getCategory().getName());
            }
        }

        BigDecimal total = categorySales.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : categorySales.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("categoryId", entry.getKey());
            item.put("categoryName", categoryNames.getOrDefault(entry.getKey(), "未分类"));
            item.put("sales", entry.getValue());
            item.put("percentage", total.compareTo(BigDecimal.ZERO) > 0
                    ? entry.getValue().multiply(new BigDecimal("100")).divide(total, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO);
            result.add(item);
        }

        // 按销售额排序
        result.sort((a, b) -> ((BigDecimal) b.get("sales")).compareTo((BigDecimal) a.get("sales")));
        return result;
    }

    /**
     * 热销商品排行
     */
    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        List<Order> allOrders = orderRepository.findAll();
        List<Product> allProducts = productRepository.findAll();

        // 统计各商品销量
        Map<Long, Integer> productSalesCount = new HashMap<>();
        Map<Long, BigDecimal> productSalesAmount = new HashMap<>();

        for (Order order : allOrders) {
            if (order.getStatus() < 0) continue;
            if (order.getItems() != null) {
                for (var item : order.getItems()) {
                    Long pid = item.getProductId();
                    productSalesCount.merge(pid, item.getQuantity(), Integer::sum);
                    BigDecimal amount = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    productSalesAmount.merge(pid, amount, BigDecimal::add);
                }
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productSalesCount.entrySet()) {
            Product product = allProducts.stream()
                    .filter(p -> p.getId().equals(entry.getKey()))
                    .findFirst().orElse(null);
            if (product != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", entry.getKey());
                item.put("productName", product.getName());
                item.put("salesCount", entry.getValue());
                item.put("salesAmount", productSalesAmount.getOrDefault(entry.getKey(), BigDecimal.ZERO));
                result.add(item);
            }
        }

        // 按销量排序
        result.sort((a, b) -> ((Integer) b.get("salesCount")).compareTo((Integer) a.get("salesCount")));
        return result.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * 用户增长趋势（最近N天）
     */
    public List<Map<String, Object>> getUserGrowth(int days) {
        List<User> allUsers = userRepository.findAll();
        List<Map<String, Object>> trend = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            long newUsers = allUsers.stream()
                    .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().toLocalDate().equals(date))
                    .count();

            // 累计用户数（到该日期为止）
            long cumulativeUsers = allUsers.stream()
                    .filter(u -> u.getCreatedAt() != null && !u.getCreatedAt().toLocalDate().isAfter(date))
                    .count();

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date.format(formatter));
            dayData.put("newUsers", newUsers);
            dayData.put("cumulativeUsers", cumulativeUsers);
            trend.add(dayData);
        }
        return trend;
    }

    /**
     * 会员等级分布
     */
    public List<Map<String, Object>> getMemberDistribution() {
        List<User> allUsers = userRepository.findAll();
        Map<Integer, String> levelNames = new HashMap<>();
        levelNames.put(0, "普通用户");
        levelNames.put(1, "银卡会员");
        levelNames.put(2, "金卡会员");
        levelNames.put(3, "钻石会员");

        Map<Integer, Long> distribution = allUsers.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getMemberLevel() != null ? u.getMemberLevel() : 0,
                        Collectors.counting()
                ));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : levelNames.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("level", entry.getKey());
            item.put("name", entry.getValue());
            item.put("count", distribution.getOrDefault(entry.getKey(), 0L));
            result.add(item);
        }
        return result;
    }

    private long countByStatus(List<Order> orders, int status) {
        return orders.stream().filter(o -> o.getStatus() != null && o.getStatus() == status).count();
    }
}
