package com.example.salvo.demo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();
    
    public Player() { }

    public Player(String first, String last) {
        this.firstName = first;
        this.lastName = last;
    }

    @JsonIgnore
    public String getLastName() {
        return lastName;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public long getId(){
        return this.id;
    }
    public String getName(){
        return this.firstName+" "+this.lastName;
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }
    public Score getScore(Game game){
        return this.scores.stream().filter(p -> p.getGame().equals(game)).findFirst().orElse(null);
    }
    public Set<Score> getScore(){
        return this.scores;
    }

}