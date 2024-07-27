package com.softuni.mehana.repository;

import com.softuni.mehana.model.entities.UserDetailsEntity;
import com.softuni.mehana.model.entities.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
}
