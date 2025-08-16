package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByEmployeeCode(String employeeCode);

    @Query("SELECT s FROM Staff s " +
            "JOIN s.user u " +
            "WHERE LOWER(u.fullName) LIKE LOWER(:keyword) " +
            "   OR LOWER(u.username) LIKE LOWER(:keyword) " +
            "   OR LOWER(u.email) LIKE LOWER(:keyword) " +
            "   OR LOWER(s.employeeCode) LIKE LOWER(:keyword)")
    Page<Staff> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
