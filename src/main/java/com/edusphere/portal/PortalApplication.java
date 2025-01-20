package com.edusphere.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PortalApplication {

	public static void main(String[] args) {

		SpringApplication.run(PortalApplication.class, args);
		System.out.println("******************************");
		System.out.println("API RUN SUCCESSFULLY");
		System.out.println("******************************");
	}

}
