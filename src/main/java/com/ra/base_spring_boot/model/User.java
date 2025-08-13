package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank @Size(min = 8)
    @Column(nullable = false, length = 255)
    private String password;

    @NotBlank @Size(max = 255)
    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @NotBlank @Email
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$", message = "Số điện thoại không hợp lệ")
    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleName role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
