package com.example.salvo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {


    @Autowired
    private GameRepository gameRep;
    @Autowired
    private GamePlayerRepository gamePlayerRep;
    @Autowired
    private PlayerRepository playerRep;



    @RequestMapping("/game_view/{gamePlayerId}")
    public GamePlayer findGamePlayer(@PathVariable Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRep.findOne(gamePlayerId);
        return gamePlayer;
    }
    @RequestMapping("/leaderboard")
    public List<Object> getleaderboard() {
        List<Object> scoreObj = new ArrayList<>();

        playerRep.findAll().stream().forEach(p -> {
            Map<String, Object> playersScore = new HashMap<>();
            playersScore.put("player", p.getUsername());
            playersScore.put("win", p.getScore().stream().filter(s -> s.getFinalScore() == 1).collect(Collectors.counting()));
            playersScore.put("lost", p.getScore().stream().filter(s -> s.getFinalScore() == 0).collect(Collectors.counting()));
            playersScore.put("tied", p.getScore().stream().filter(s -> s.getFinalScore() == 0.5).collect(Collectors.counting()));
            playersScore.put("total", p.getScore().stream().map(e-> e.getFinalScore()).reduce((double) 0, (a, b) -> a + b));
            scoreObj.add(playersScore);
                });
        return scoreObj;
    }

    @RequestMapping("/games")
    public List<Object> getGames() {

        List<Object> gamesObj = new ArrayList<>();
        gameRep.findAll().stream().forEach(game -> {
            Map<String, Object> games = new HashMap<>();
            games.put("created", game.date.toString());
            games.put("id", game.getId());
            games.put("gamePlayers", findGamePlayersfromGame(game));
            games.put("scores", findScoresfromGame(game));
            gamesObj.add(games);
        });
return gamesObj;
    }


    public List<Object> findGamePlayersfromGame(Game game){
        List<Object> playerObj = new ArrayList<>();
        game.getGamePlayers().forEach(gamePl -> {
                Map<String, Object> gamePlayers = new HashMap<>();
                gamePlayers.put("id", gamePl.getId());
                gamePlayers.put("player", findPlayerfromGamePlayer(gamePl) );
                gamePlayers.put("ships", findShipsfromGamePlayer(gamePl));
                gamePlayers.put("salvoes", findSalvoesfromGamePlayer(gamePl));
                playerObj.add(gamePlayers);

        });
        return playerObj;
    }
    public Object findPlayerfromGamePlayer(GamePlayer gamePl){
        Player player = gamePl.getPlayer();
        Map<String, Object> myplayer = new HashMap<>();
        myplayer.put("id", player.getId());
        myplayer.put("name",player.getUsername());
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

    public List<Object> findSalvoesfromGamePlayer(GamePlayer gamePl){
        List<Object> salvoObj = new ArrayList<>();
        gamePl.getSalvoes().stream().forEach(salvo -> {
            Map<String, Object> salvoes = new HashMap<>();
            salvoes.put("turn", salvo.getTurn());
            salvoes.put("salvoes_location",salvo.getSalvoesLocations());
            salvoObj.add(salvoes);
        });
        return salvoObj;
    }
    public List<Object> findScoresfromGame(Game game){
        List<Object> scoreObj = new ArrayList<>();
        game.getScore().stream().forEach(score -> {
            Map<String, Object> scores = new HashMap<>();
            scores.put("Player", score.getPlayer());
            scores.put("Score",score.getFinalScore());
            scoreObj.add(scores);
        });
        return scoreObj;
    }


}