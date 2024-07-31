package com.softuni.mehana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MehanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MehanaApplication.class, args);
	}

}
