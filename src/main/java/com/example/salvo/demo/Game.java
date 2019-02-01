package com.example.salvo.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    Date date ;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Game() {}

    public Game(Date date) {
       this.date = date;
    }

    public String toString() {
        return date.toString();
    }

    public void addGame(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public long getId(){
        return this.id;
    }

    @JsonIgnore
    public Set<GamePlayer> getGamePlayers(){
        return this.gamePlayers;
    }
    @JsonIgnore
    public Set<Score> getScore(){
        return this.scores;
    }
    /*
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }*/

}