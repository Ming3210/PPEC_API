package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

=======
import org.springframework.stereotype.Repository;

@Repository
>>>>>>> b09d1708296b754049d74cc987095ae7e7cb2c2a
public interface DepartmentRepository extends JpaRepository<Departments, Long> {
}
