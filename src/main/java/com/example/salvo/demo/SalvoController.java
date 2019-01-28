package com.example.salvo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SalvoController {


    @Autowired
    private GameRepository gameRep;
    private PlayerRepository playerRepository;


    @RequestMapping("/games")
    public List<Object> getGames() {


        List<Object> gamesObj = new ArrayList<Object>();
        gameRep.findAll().stream().forEach(el -> {
            //List<Object> gamePlayer = new ArrayList<Object>();
            //gamePlayer.put("id", )
            Map<String, Object> games = new HashMap<>();
            games.put("created", el.date);
            games.put("id", el.id);
            gamesObj.add(games);
        });
return gamesObj;
       // return gameRep.findAll().stream().map(e -> e.id).collect(toSet());
    }

}