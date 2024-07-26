package com.softuni.mehana.repository;

import com.softuni.mehana.model.entities.UserRoleEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByRole(UserRoleEnum userRoleEnum);
}
