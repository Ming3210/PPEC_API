package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUserId(Long userId);

}
