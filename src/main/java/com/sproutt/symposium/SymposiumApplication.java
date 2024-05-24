package com.sproutt.symposium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SymposiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SymposiumApplication.class, args);
	}
}
