package com.softuni.mehana.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    private UserRoleEntity role;

    @OneToOne
    private UserDetailsEntity userDetails;

    @OneToOne
    private CartEntity cart;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
}
