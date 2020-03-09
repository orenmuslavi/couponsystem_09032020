package com.osa;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class CouponManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponManagementSystemApplication.class, args);
		System.out.println("[]::]>>>>>>>>>>>>> GO");	
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		// encode company and customer passwords
		// BCryptPasswordEncoder implements PasswordEncoder interface
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public SpringApplicationContext springApplicationContext() {
		// get access to spring bean container
		return new SpringApplicationContext();
	}

}
