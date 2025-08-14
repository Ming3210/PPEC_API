package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Departments, Long> {
}
