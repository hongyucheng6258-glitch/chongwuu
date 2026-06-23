package com.petshop.repository;

import com.petshop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Review> findByOrderId(Long orderId);
    boolean existsByOrderIdAndProductId(Long orderId, Long productId);
    long countByProductId(Long productId);
}
