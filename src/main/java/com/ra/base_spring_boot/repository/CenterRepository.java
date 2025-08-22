package com.ra.base_spring_boot.repository;


import com.ra.base_spring_boot.model.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    // Tìm tất cả centers của một user theo userId
    Page<Center> findByUserId(Long userId, Pageable pageable);

    // Tìm theo fullName User hoặc address
    Page<Center> findByUserFullNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String fullName, String address, Pageable pageable);

}
