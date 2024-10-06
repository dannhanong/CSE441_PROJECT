package com.ktpm1.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class RestaurantBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantBeApplication.class, args);
	}

}
