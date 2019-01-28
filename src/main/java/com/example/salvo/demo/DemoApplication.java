package com.example.salvo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRep) {
		return (args) -> {

			repository.save(new Player("Jack", "Bauer"));
			repository.save(new Player("Chloe", "O'Brian"));
			repository.save(new Player("Kim", "Bauer"));
			repository.save(new Player("David", "Palmer"));
			repository.save(new Player("Michelle", "Dessler"));
			Date date = new Date();
			gameRep.save(new Game(date));
			Date date1h = Date.from(date.toInstant().plusSeconds(3600));
			gameRep.save(new Game(date1h));
			Date date2h = Date.from(date.toInstant().plusSeconds(7200));
			gameRep.save(new Game(date2h));


		};
	}
}

