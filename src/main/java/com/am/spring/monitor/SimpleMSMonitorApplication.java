package com.am.spring.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimpleMSMonitorApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMSMonitorApplication.class);

	public static void main(String[] args) {
		LOGGER.info("$$$###Starting Microservice Monitor###$$$");
		SpringApplication.run(SimpleMSMonitorApplication.class, args);

	}

}

