package com.softuni.mehana.repository;

import com.softuni.mehana.model.entities.PromoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoRepository extends JpaRepository<PromoEntity, Long> {
}
