package com.basic.template.backend_template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTemplateApplication.class, args);
	}

}
