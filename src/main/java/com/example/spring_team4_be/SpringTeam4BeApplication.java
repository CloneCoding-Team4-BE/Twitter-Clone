package com.example.spring_team4_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringTeam4BeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTeam4BeApplication.class, args);
	}

}
