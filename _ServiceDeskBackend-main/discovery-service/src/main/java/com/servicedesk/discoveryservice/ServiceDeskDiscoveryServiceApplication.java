package com.servicedesk.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceDeskDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDeskDiscoveryServiceApplication.class, args);
	}

}
