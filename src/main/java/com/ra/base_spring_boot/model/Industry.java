package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "industries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @ManyToMany(mappedBy = "industries", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Partner> partners = new HashSet<>();
}
