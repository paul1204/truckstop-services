package com.truckstopservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableFeignClients(basePackages = "com.truckstopservices.processing.client")
public class TruckstopServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruckstopServicesApplication.class, args);
	}

}
