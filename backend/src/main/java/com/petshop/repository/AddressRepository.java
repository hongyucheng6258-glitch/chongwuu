package com.petshop.repository;

import com.petshop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserIdOrderByIsDefaultDesc(Long userId);
    List<Address> findByUserIdAndIsDefault(Long userId, Integer isDefault);
}
