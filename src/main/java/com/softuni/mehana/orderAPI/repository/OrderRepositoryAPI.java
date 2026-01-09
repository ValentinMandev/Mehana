package com.softuni.mehana.orderAPI.repository;

import com.softuni.mehana.orderAPI.model.entities.OrderEntityAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoryAPI extends JpaRepository<OrderEntityAPI, Long> {
    List<OrderEntityAPI> findAllByUserId(Long userId);
}
