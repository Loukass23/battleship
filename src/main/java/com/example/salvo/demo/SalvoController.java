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
    @Autowired
    private ShipRepository shipRep;

    @RequestMapping("/games")
    public List<Object> getGames() {

        List<Object> gamesObj = new ArrayList<>();
        gameRep.findAll().stream().forEach(game -> {
            Map<String, Object> games = new HashMap<>();
            games.put("created", game.date.toString());
            games.put("id", game.getId());
            games.put("gamePlayers", findPlayersfromGame(game));
            gamesObj.add(games);
        });
return gamesObj;
    }


    public List<Object> findPlayersfromGame(Game game){
        List<Object> playerObj = new ArrayList<>();
        gamePlayerRep.findAll().stream().forEach(gamePl -> {
            if (Objects.equals(gamePl.getGame().getId(), game.getId())) {
                Map<String, Object> gamePlayers = new HashMap<>();
                gamePlayers.put("id", gamePl.getId());
                gamePlayers.put("ships", findShipsfromGamePlayer(gamePl));
                playerObj.add(gamePl);
                System.out.println(gamePl);
            }
        });
        return playerObj;
    }
    public List<Object> findShipsfromGamePlayer(GamePlayer gamePl){
        List<Object> shipsObj = new ArrayList<>();
        shipRep.findAll().stream().forEach(ship -> {

            if (Objects.equals(gamePl.getId(), ship.getGamePlayer().getId())){
                Map<String, Object> gamePlayers = new HashMap<>();
                gamePlayers.put("type", ship.getType());
                //gamePlayers.put("ships", gamePl.getShips().toString());
                shipsObj.add(gamePlayers);
            }
        });
        return shipsObj;
    }

}