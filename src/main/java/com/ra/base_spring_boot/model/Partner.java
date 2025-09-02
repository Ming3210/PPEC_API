package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.PartnerStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "partner_code", nullable = false, unique = true, length = 50)
    private String partnerCode;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @PositiveOrZero
    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;

    @PositiveOrZero
    @Column(name = "number_of_courses")
    private Integer numberOfCourses;

    @NotBlank
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PartnerStatus status = PartnerStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "partner_industries",
            joinColumns = @JoinColumn(name = "partner_id"),
            inverseJoinColumns = @JoinColumn(name = "industry_id")
    )
    @Builder.Default
    private Set<Industry> industries = new HashSet<>();
    private RoleName role = RoleName.SCHOOL_ADMIN;
}
