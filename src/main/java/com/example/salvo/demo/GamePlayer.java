package com.example.salvo.demo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;


    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();



    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player ) {
        this.game = game;
        this.player = player;
        this.date = new Date();



    }

    public void addShip(Ship ship) {
         this.ships.add(ship);
        System.out.println(ship.type);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    @Override
    public String toString() {
        return "game: " + this.game + "- Player: " + this.player;
    }

    public long getId(){
        return this.id;
    }

    public Game getGame() {
        return this.game;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Set<Ship> getShips() {
        return this.ships;
    }
}