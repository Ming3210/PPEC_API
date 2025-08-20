package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    boolean existsByPartnerCode(String partnerCode);
    boolean existsByPartnerCodeAndIdNot(String partnerCode, Long id);
    boolean existsByName(String name);
    Page<Partner> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    @Query("SELECT p FROM Partner p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.partnerCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Partner> searchByKeyword(String keyword, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Partner p LEFT JOIN FETCH p.industries " +
            "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Partner> searchWithIndustries(@Param("keyword") String keyword, Pageable pageable);

}
