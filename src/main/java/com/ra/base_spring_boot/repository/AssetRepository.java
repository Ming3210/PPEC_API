package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Asset;
import com.ra.base_spring_boot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Page<Asset> findByAssignedUser(User user, Pageable pageable);
}
