package com.example.BankAPIWithMongoDB;

import com.example.BankAPIWithMongoDB.util.MongoDataCopy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BankApiWithMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApiWithMongoDbApplication.class, args);

	}

	@Value("${quarter}")
	Integer quarter;

	@Bean
	CommandLineRunner commandLineRunner (String[] args) {
		return runner ->{
			MongoDataCopy mongoDataCopy = new MongoDataCopy("mongodb://localhost:27017",
															"bank",
															"bank_V2",
															"accounts",
															"accounts");


			mongoDataCopy.copyQuarter(2023,quarter);
		};
	}


}
