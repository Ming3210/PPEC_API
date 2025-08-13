package com.ra.base_spring_boot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseObject
{
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "passwordF")
    private String password;

    private Boolean status;


}
