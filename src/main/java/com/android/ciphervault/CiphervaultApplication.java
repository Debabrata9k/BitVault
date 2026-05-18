package com.android.ciphervault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CiphervaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(CiphervaultApplication.class, args);
	}

}
