package com.example.salvo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/game_view/{gamePlayerId}")
    public GamePlayer findGamePlayer(@PathVariable Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRep.findOne(gamePlayerId);
        System.out.println(gamePlayer);
        return gamePlayer;
    }

    @RequestMapping("/games")
    public List<Object> getGames() {

        List<Object> gamesObj = new ArrayList<>();
        gameRep.findAll().stream().forEach(game -> {
            Map<String, Object> games = new HashMap<>();
            games.put("created", game.date.toString());
            games.put("id", game.getId());
            games.put("gamePlayers", findGamePlayersfromGame(game));
            gamesObj.add(games);
        });
return gamesObj;
    }


    public List<Object> findGamePlayersfromGame(Game game){
        List<Object> playerObj = new ArrayList<>();
        game.getGamePlayers().forEach(gamePl -> {
                Map<String, Object> gamePlayers = new HashMap<>();
                gamePlayers.put("id", gamePl.getId());
                gamePlayers.put("player", findPlayersfromGamePlayer(gamePl) );
                gamePlayers.put("ships", findShipsfromGamePlayer(gamePl));
                playerObj.add(gamePlayers);

        });
        return playerObj;
    }
    public Object findPlayersfromGamePlayer(GamePlayer gamePl){
        Player player = gamePl.getPlayer();
        Map<String, Object> myplayer = new HashMap<>();
        myplayer.put("id", player.getId());
        myplayer.put("name",player.getName());
        return myplayer;
    }
    public List<Object> findShipsfromGamePlayer(GamePlayer gamePl){
        List<Object> shipsObj = new ArrayList<>();
        gamePl.getShips().stream().forEach(ship -> {
                Map<String, Object> ships = new HashMap<>();
                ships.put("type", ship.getType());
                ships.put("location",ship.getLocations());
                shipsObj.add(ships);
        });
        return shipsObj;
    }

}