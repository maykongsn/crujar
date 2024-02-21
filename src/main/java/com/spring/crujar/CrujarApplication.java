package com.spring.crujar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.spring.crujar.domain")
public class CrujarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrujarApplication.class, args);
	}
}
