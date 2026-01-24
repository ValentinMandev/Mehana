package com.softuni.mehana.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SelfPingScheduler {

    private long pingCount = 0;

    @Scheduled(fixedRate = 60000)
    public void keepAlive() {
        pingCount++;
    }
}
