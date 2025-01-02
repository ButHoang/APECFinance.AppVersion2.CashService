package com.apec_finance.cash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class V2Application {

	public static void main(String[] args) {
		SpringApplication.run(V2Application.class, args);
	}

}
