package com.jumio.callback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CallbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallbackApplication.class, args);
	}

}
