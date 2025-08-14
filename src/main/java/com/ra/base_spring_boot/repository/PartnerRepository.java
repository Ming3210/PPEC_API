package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
    boolean existsByPartnerCode(String partnerCode);
    boolean existsByPartnerCodeAndIdNot(String partnerCode, Long id);
    boolean existsByName(String name);
    Page<Partner> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
