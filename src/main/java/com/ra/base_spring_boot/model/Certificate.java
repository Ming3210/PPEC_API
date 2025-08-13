package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.CertificateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "examDate")
    private LocalDateTime examDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CertificateStatus status;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "createdBy", nullable = false)
    private Long createdBy;

    @Column(name = "updatedBy")
    private Long updatedBy;

    @ManyToOne
    @JoinColumn(name = "createdBy", insertable = false, updatable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "updatedBy", insertable = false, updatable = false)
    private User updater;

    @OneToMany(mappedBy = "certificate")
    private List<UserCertificate> userCertificates;
}