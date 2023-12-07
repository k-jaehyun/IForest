package com.sparta.iforest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IForestApplication {

	public static void main(String[] args) {
		SpringApplication.run(IForestApplication.class, args);
	}

}
