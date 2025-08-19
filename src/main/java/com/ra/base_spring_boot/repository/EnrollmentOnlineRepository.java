package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.EnrollmentOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentOnlineRepository extends JpaRepository<EnrollmentOnline, Long> {
}
