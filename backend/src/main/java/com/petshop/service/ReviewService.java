package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.ReviewRequest;
import com.petshop.entity.Review;
import com.petshop.repository.OrderRepository;
import com.petshop.entity.Order;
import com.petshop.repository.ProductRepository;
import com.petshop.repository.ReviewRepository;
import com.petshop.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Review create(ReviewRequest req) {
        Long userId = CurrentUser.getUserId();
        // 校验订单是否存在且属于当前用户
        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权评价该订单");
        }
        if (order.getStatus() != 3 && order.getStatus() != 4) {
            throw new BusinessException("订单状态不支持评价");
        }
        if (reviewRepository.existsByOrderIdAndProductId(req.getOrderId(), req.getProductId())) {
            throw new BusinessException("已评价过该商品");
        }
        Review review = new Review();
        review.setUserId(userId);
        review.setOrderId(req.getOrderId());
        review.setProductId(req.getProductId());
        review.setRating(req.getRating());
        review.setContent(req.getContent());
        review.setImages(req.getImages());
        return reviewRepository.save(review);
    }

    public List<Review> listByProduct(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public List<Review> listByUser() {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(CurrentUser.getUserId());
    }

    /**
     * 管理员获取所有评价
     */
    public List<Review> listAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("评价不存在"));
        if (!review.getUserId().equals(CurrentUser.getUserId()) && !CurrentUser.isAdmin()) {
            throw new BusinessException(403, "无权操作");
        }
        reviewRepository.delete(review);
    }
}
