package com.softuni.mehana.utils;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SelfPingScheduler {

    @Autowired
    private EntityManager entityManager;

    @Scheduled(fixedRate = 6000) // 14 минути
    @Transactional
    public void keepAlive() {
        entityManager.createNativeQuery("SELECT 1").getSingleResult();
    }
}
