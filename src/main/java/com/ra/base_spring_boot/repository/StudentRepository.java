package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Student;
import com.ra.base_spring_boot.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentCode(@NotBlank @Size(max = 50) String studentCode);

    Student findByUser_Username(@NotBlank @Size(max = 50) String userUsername);

    Optional<Student> findByUserId(Long userId);

    Student findByUser(User user);
}
