package com.softuni.mehana.utils;

import com.softuni.mehana.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SelfPingScheduler {

    @Autowired
    private UserRepository userRepository; // или друго repository

    @Scheduled(fixedRate = 6000) // 14 минути
    public void keepAlive() {
        userRepository.count();
    }
}
