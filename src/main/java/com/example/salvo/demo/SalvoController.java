package com.example.salvo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ShipRepository shipRep;


    @RequestMapping("/login")
    public Player loggedPlayer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return playerRep.findByUsername(authentication.getName());
        }
        else return null;
    }


    @RequestMapping("/game_view/{gamePlayerId}")
    public GamePlayer findGamePlayer(@PathVariable Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRep.findOne(gamePlayerId);
        if(loggedPlayer().gamePlayers.stream().anyMatch(e -> e == gamePlayer)) return gamePlayer;
        else return  null;
    }
    @RequestMapping("/game/{gameId}")
    public Map<String, Object>findGame(@PathVariable Long gameId) {
        Game game = gameRep.findOne(gameId);
        Map<String, Object> map = new HashMap<>();
        if(game.getPlayers().contains(loggedPlayer())) {
            map.put("player",loggedPlayer());
            map.put("game",game);
            map.put("error",null);
            return map;

        }
        else    {
            map.put("player",loggedPlayer());
            map.put("error",new ResponseEntity<String>("Unauthorized PLayer", HttpStatus.FORBIDDEN ));
            return map;

        }

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

    @RequestMapping("/players")
    public Map<String, Object> getPlayers() {
        Map<String, Object> map = new HashMap<>();
        map.put("player",loggedPlayer());
        map.put("players",playerRep.findAll());
        return map;
    }
    @RequestMapping("/games")
    public Map<String, Object> getGames() {

        List<Object> gamesObj = new ArrayList<>();

        gameRep.findAll().stream().forEach(game -> {
            Map<String, Object> games = new HashMap<>();
            games.put("created", game.date.toString());
            games.put("id", game.getId());
            games.put("gamePlayers", findGamePlayersfromGame(game));
            games.put("scores", findScoresfromGame(game));
            gamesObj.add(games);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("player",loggedPlayer());
        map.put("games",gamesObj);
return map;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String username, @RequestParam String password) {

        if ( username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRep.findByUsername(username) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRep.save(new Player( username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> registerGame(

            @RequestParam  String playerName) {
        Date date = new Date();
        if ( playerName.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Player player = playerRep.findByUsername(playerName);
        if (player ==  null) {
            return new ResponseEntity<>("Unknown User", HttpStatus.FORBIDDEN);
        }
        Game game = new Game( date);
        gameRep.save(game);
        gamePlayerRep.save(new GamePlayer(game , player));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> addShips( @RequestParam List<Object> shipsObj, @PathVariable Long gameplayerId){

    GamePlayer gamePlayer = gamePlayerRep.findOne(gameplayerId);
    if(gamePlayer.getPlayer() != loggedPlayer()) return new ResponseEntity<>("Unauthorized User", HttpStatus.FORBIDDEN);
        System.out.println(shipsObj.toString());
        shipsObj.stream().forEach(e -> {
            System.out.println(e.toString());
           /* Ship ship = new Ship(e.getType(), e.getLocations());
            shipRep.save(ship);
            gamePlayer.addShip(ship);
            gamePlayerRep.save(gamePlayer);
            shipRep.save(ship);*/
        } );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{gameId}/player", method = RequestMethod.POST)
    public ResponseEntity<Object> addPlayer(String playerName, @PathVariable Long gameId){
        if ( playerName.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Player player = playerRep.findByUsername(playerName);
        Game game = gameRep.findOne(gameId);

        if(game.getGamePlayers().stream().count() >= 2) return new ResponseEntity<>("Too many players", HttpStatus.FORBIDDEN);
        if(game.getGamePlayers().stream().anyMatch(e -> e.getPlayer() == player)) return new ResponseEntity<>("Player already in game", HttpStatus.FORBIDDEN);


        GamePlayer gamePlayer = new GamePlayer(game , player);
        gamePlayerRep.save(gamePlayer);
        game.addGamePlayer(gamePlayer);
        gameRep.save(game);
        gamePlayerRep.save(gamePlayer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}