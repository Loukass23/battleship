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
	public CommandLineRunner initData(PlayerRepository playerRep, GameRepository gameRep, GamePlayerRepository gamePlayerRep) {
		return (args) -> {


			Date date = new Date();
			Date date1h = Date.from(date.toInstant().plusSeconds(3600));
			Date date2h = Date.from(date.toInstant().plusSeconds(7200));


			Player p1 = new Player("Jack", "Bauer");
			Player p2 = new Player("Chloe", "O'Brian" );
			Player p3 = new Player("Kim", "Bauer");
			Player p4 = new Player("Tony", "Almeida");
			playerRep.save(p1);
			playerRep.save(p2);
			playerRep.save(p3);
			playerRep.save(p4);

			Game g1 = new Game(date);
			Game g2 = new Game(date1h);
			Game g3 = new Game(date2h);
			gameRep.save(g1);
			gameRep.save(g2);
			gameRep.save(g3);

			GamePlayer gp1 = new GamePlayer(g1, p1);
			gamePlayerRep.save(gp1);


			GamePlayer gp2 = new GamePlayer(g2, p2);
			gamePlayerRep.save(gp2);


		};
	}
}

