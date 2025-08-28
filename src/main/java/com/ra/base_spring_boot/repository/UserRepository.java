package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndRole(Long id, RoleName role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.role <> :role " +
            "AND (:keyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> findAllExceptAdmin(@Param("role") RoleName role,
                                  @Param("keyword") String keyword,
                                  Pageable pageable);
}