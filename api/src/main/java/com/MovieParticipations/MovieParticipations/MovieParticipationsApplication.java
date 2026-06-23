package com.MovieParticipations.MovieParticipations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MovieParticipationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieParticipationsApplication.class, args);
	}

}
