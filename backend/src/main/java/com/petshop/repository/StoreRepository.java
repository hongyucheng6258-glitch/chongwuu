package com.petshop.repository;

import com.petshop.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByStatus(Integer status);

    @Query("SELECT s FROM Store s WHERE s.status = 1 AND s.name LIKE %:keyword%")
    List<Store> search(@Param("keyword") String keyword);

    boolean existsByName(String name);
}
