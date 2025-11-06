package com.learnings.intern_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InternServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternServiceApplication.class, args);
	}

}
