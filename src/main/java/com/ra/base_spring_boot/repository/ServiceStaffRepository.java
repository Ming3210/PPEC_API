package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.ServiceStaff;
import com.ra.base_spring_boot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceStaffRepository extends JpaRepository<ServiceStaff, Long> {
    @Query("SELECT COUNT(s) > 0 FROM ServiceStaff s WHERE s.staffCode = ?1")
    boolean isCheckStaffCode(String staffCode);
    @Query("SELECT s FROM ServiceStaff s " +
            "JOIN s.user u " +
            "JOIN s.center c " +
            "WHERE (:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ServiceStaff> search(@Param("keyword") String keyword, Pageable pageable);

}
