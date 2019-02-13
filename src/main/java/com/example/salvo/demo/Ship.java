package com.example.salvo.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String type ;
    private boolean horizontal ;


    @ElementCollection
    @Column(name="ShipLocation")
    private List<String> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;



    public Ship() {}

    public Ship(String type) {
        this.type = type;
    }
    public Ship(String type, List<String> location, boolean horizontal) {
        this.type = type;
        this.locations = location;
        this.horizontal = horizontal;
    }
    public void setGamePlayer(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public long getId(){
        return this.id;
    }
    public String getType(){
        return this.type;
    }

    public List<String> getLocations(){
        return this.locations;
    }

    @Override
    public String toString() {
        return this.type;
    }

    @JsonIgnore
    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }
}
