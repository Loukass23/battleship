package com.example.salvo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRep, GameRepository gameRep,
									  GamePlayerRepository gamePlayerRep, ShipRepository shipRep,
									  SalvoRepository salvRep, ScoreRepository scoreRep ) {
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

			//Ships Player 1
			List<String> shipLocation1 = new ArrayList<>();
			shipLocation1.add("H8");
			shipLocation1.add("H9");
			shipLocation1.add("H10");
			List<String> shipLocation2 = new ArrayList<>();
			shipLocation2.add("A6");
			shipLocation2.add("A7");
			shipLocation2.add("A8");
			shipLocation2.add("A9");
			List<String> shipLocation3 = new ArrayList<>();
			shipLocation3.add("C4");
			shipLocation3.add("D4");
			shipLocation3.add("E4");
			shipLocation3.add("F4");
			Ship s1 = new Ship("Destroyer",shipLocation1);
			Ship s2 = new Ship("Submarine",shipLocation2);
			Ship s3 = new Ship("Patrol Boat",shipLocation3);
			GamePlayer gp1 = new GamePlayer(g1, p1);

			gamePlayerRep.save(gp1);
			gp1.addShip(s1);
			gp1.addShip(s2);
			gp1.addShip(s3);
			shipRep.save(s1);
			shipRep.save(s2);
			shipRep.save(s3);
			gamePlayerRep.save(gp1);

            //Ships Player 2
			List<String> shipLocation4 = new ArrayList<String>();;
			shipLocation4.add("I8");
			shipLocation4.add("I9");
			shipLocation4.add("I10");
			List<String> shipLocation5 = new ArrayList<String>();;
			shipLocation5.add("A1");
			shipLocation5.add("A2");
			shipLocation5.add("A3");
			shipLocation5.add("A4");
			List<String> shipLocation6 = new ArrayList<String>();;
			shipLocation6.add("C4");
			shipLocation6.add("D4");
			shipLocation6.add("E4");
			shipLocation6.add("F4");
            Ship s4 = new Ship("Destroyer",shipLocation4);
            Ship s5 = new Ship("Submarine",shipLocation5);
            Ship s6 = new Ship("Patrol Boat",shipLocation6);
			GamePlayer gp2 = new GamePlayer(g1, p2);


            gamePlayerRep.save(gp2);
			gp2.addShip(s4);
            gp2.addShip(s5);
            gp2.addShip(s6);
            shipRep.save(s4);
            shipRep.save(s5);
            shipRep.save(s6);
            gamePlayerRep.save(gp2);

			List<String> salvoLocation1 = new ArrayList<>();
			salvoLocation1.add("C4");
			salvoLocation1.add("D4");
			List<String> salvoLocation2 = new ArrayList<>();
			salvoLocation2.add("H8");
			salvoLocation2.add("F1");
			List<String> salvoLocation3 = new ArrayList<>();
			salvoLocation3.add("I10");
			salvoLocation3.add("A10");
			Salvo salv1 = new Salvo(1, salvoLocation1);
			Salvo salv2 = new Salvo(1, salvoLocation2);
			Salvo salv3 = new Salvo(2, salvoLocation3);
			gp2.addSalvo(salv2);
			gp1.addSalvo(salv1);
			salvRep.save(salv1);
			salvRep.save(salv2);
			gamePlayerRep.save(gp2);
			gamePlayerRep.save(gp1);

			gp1.addSalvo(salv3);
			salvRep.save(salv3);
			gamePlayerRep.save(gp1);

			Score scr1 = new Score(g1,p1, (double) 1);
			scoreRep.save(scr1);
			Score scr2 = new Score(g1,p2, (double) 0);
			scoreRep.save(scr2);
			Score scr3 = new Score(g2,p1, 0.5);
			scoreRep.save(scr3);
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

