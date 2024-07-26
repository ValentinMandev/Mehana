package com.softuni.mehana.repository;

import com.softuni.mehana.model.entities.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {
}
