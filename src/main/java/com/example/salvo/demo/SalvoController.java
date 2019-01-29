package com.example.salvo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SalvoController {


    @Autowired
    private GameRepository gameRep;
    @Autowired
    private GamePlayerRepository gamePlayerRep;


    @RequestMapping("/games")
    public List<Object> getGames() {


        List<Object> gamesObj = new ArrayList<>();
        gameRep.findAll().stream().forEach(game -> {

            List<Object> playerObj = new ArrayList<>();

            gamePlayerRep.findAll().stream().forEach(e -> {
                if (Objects.equals(e.getGame().getId(), game.getId())) {
                    Map<String, Object> gamePlayers = new HashMap<>();
                    gamePlayers.put("id", e.getId());
                    playerObj.add(e);
                }
            });

            Map<String, Object> games = new HashMap<>();
            games.put("created", game.date.toString());
            games.put("id", game.getId());
            games.put("gamePlayers", playerObj );
            gamesObj.add(games);
        });
return gamesObj;
    }

}