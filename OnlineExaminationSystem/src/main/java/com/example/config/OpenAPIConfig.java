package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {
	
	//Swagger Configuration
		@Bean
		public OpenAPI customApi() {
			
			return new OpenAPI().info(new Info().title("Online Examination System API")
					          .version("1.0").description("REST API for managing Examination details,"
					          		+ "Students take online exams and get scores automatically")
					          .contact(new Contact().name("Praveen Dokkari")
					        		                 .email("dokkaripraveen2@gmail.com")));
		}

}
