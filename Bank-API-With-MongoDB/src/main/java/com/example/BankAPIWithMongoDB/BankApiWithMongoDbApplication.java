package com.example.BankAPIWithMongoDB;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ConcurrentLruCache;

import java.util.Random;


@SpringBootApplication
public class BankApiWithMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApiWithMongoDbApplication.class, args);

	}

	@Bean
	CommandLineRunner commandLineRunner (String[] args) {
		return runner -> {

		};
	}

}
