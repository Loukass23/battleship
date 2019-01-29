package com.example.salvo.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    public long id;

    String type ;

    @ElementCollection
    @Column(name="ShipLocation")
    private List<String> shipLocations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;



    public Ship() {}

    public Ship(String type) {
        this.type = type;
    }

    @JsonIgnore
    public Player getGamePlayer() {
        return gamePlayer.getPlayer();
    }
}
