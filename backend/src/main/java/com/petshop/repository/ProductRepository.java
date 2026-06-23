package com.petshop.repository;

import com.petshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatus(Integer status, Pageable pageable);

    Page<Product> findByStatusAndCategoryId(Integer status, Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = :status AND (p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> search(@Param("status") Integer status, @Param("keyword") String keyword, Pageable pageable);
}
