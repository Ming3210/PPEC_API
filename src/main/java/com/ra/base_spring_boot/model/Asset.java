package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.AssetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AssetStatus status;

    @Column(name = "purchaseDate")
    private LocalDateTime purchaseDate;

    @Column(name = "purchaseValue", precision = 15, scale = 2)
    private Double purchaseValue;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "assignedUserId")
    private Long assignedUserId;

    @Column(name = "createdBy", nullable = false)
    private Long createdBy;

    @Column(name = "updatedBy")
    private Long updatedBy;

    @ManyToOne
    @JoinColumn(name = "assignedUserId", insertable = false, updatable = false)
    private User assignedUser;

    @ManyToOne
    @JoinColumn(name = "createdBy", insertable = false, updatable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "updatedBy", insertable = false, updatable = false)
    private User updater;
}