package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "service_staffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "hometown", length = 100)
    private String hometown;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    private String staffCode;
    @ManyToOne
    @JoinColumn(name = "center_id")
    private Center center;

    @NotBlank
    @Column(name = "position", length = 100)
    private String position = "Nhân viên dịch vụ";
}
