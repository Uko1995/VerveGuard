package com.example.VerveGaurd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class VerveGaurdApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerveGaurdApplication.class, args);
	}

}
