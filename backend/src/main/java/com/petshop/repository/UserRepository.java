package com.petshop.repository;

import com.petshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByGiteeId(Long giteeId);
    boolean existsByUsername(String username);
}
