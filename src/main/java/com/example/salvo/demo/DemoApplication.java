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
	public CommandLineRunner initData(PlayerRepository playerRep, GameRepository gameRep, GamePlayerRepository gamePlayerRep, ShipRepository shipRep) {
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

			Ship s1 = new Ship("Destroyer");
			Ship s2 = new Ship("Submarine");
			Ship s3 = new Ship("Patrol Boat");
			GamePlayer gp1 = new GamePlayer(g1, p1);
            gamePlayerRep.save(gp1);
            gp1.addShip(s1);
            gp1.addShip(s2);
            gp1.addShip(s3);
            shipRep.save(s1);
            shipRep.save(s2);
            shipRep.save(s3);
            gamePlayerRep.save(gp1);

            Ship s4 = new Ship("Destroyer");
            Ship s5 = new Ship("Submarine");
            Ship s6 = new Ship("Patrol Boat");
			GamePlayer gp2 = new GamePlayer(g1, p2);
            gamePlayerRep.save(gp2);
			gp2.addShip(s4);
            gp2.addShip(s5);
            gp2.addShip(s6);
            shipRep.save(s4);
            shipRep.save(s5);
            shipRep.save(s6);
            gamePlayerRep.save(gp2);


/*
			GamePlayer gp3 = new GamePlayer(g2, p3);
			gamePlayerRep.save(gp3);

			GamePlayer gp4 = new GamePlayer(g2, p4);
			gamePlayerRep.save(gp4);

			GamePlayer gp5 = new GamePlayer(g3, p1);
			gamePlayerRep.save(gp5);

			GamePlayer gp6 = new GamePlayer(g3, p3);
			gamePlayerRep.save(gp6);*/




		};
	}
}

