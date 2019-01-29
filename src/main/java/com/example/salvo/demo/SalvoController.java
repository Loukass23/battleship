package com.example.salvo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class SalvoController {


    @Autowired
    private GameRepository gameRep;
    @Autowired
    private GamePlayerRepository gamePlayerRep;


    @RequestMapping("/games")
    public List<Object> getGames() {


        List<Object> gamesObj = new ArrayList<Object>();
        gameRep.findAll().stream().forEach(el -> {

            List<Object> playerObj = new ArrayList<Object>();

            gamePlayerRep.findAll().stream().forEach(e -> {
                if (Objects.equals(e.getGame().id, el.id)) {
                    Map<String, Object> gamePlayers = new HashMap<>();
                    gamePlayers.put("id", e.id);
                    playerObj.add(e);
                }
            });

            Map<String, Object> games = new HashMap<>();
            games.put("created", el.date.toString());
            games.put("id", el.id);
            games.put("gamePlayers", playerObj );
            gamesObj.add(games);
        });
return gamesObj;
       // return gameRep.findAll().stream().map(e -> e.id).collect(toSet());
    }

}