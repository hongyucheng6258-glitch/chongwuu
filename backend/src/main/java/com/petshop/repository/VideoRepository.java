package com.petshop.repository;

import com.petshop.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Video> findByProductId(Long productId);
    List<Video> findByCategory(String category);
}
