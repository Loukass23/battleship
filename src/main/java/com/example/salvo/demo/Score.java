package com.example.salvo.demo;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;



@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Double score ;
    private Date finshDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;


    public Score() {}

    public Score(Game game, Player player, Double score){
        this.player = player;
        this.game = game;
        this.score = score;
    }

    public Score(Double score, Date finishDate) {
        this.score = score;
        this.finshDate = finishDate;

    }

    public void setGame(Game game){ this.game  = game; }

    public void setPlayer(Player player){ this.player  = player; }

    public long getId(){
        return this.id;
    }

    public Double getFinalScore(){return this.score;}

    public Game getGame() {
        return this.game;
    }

    public Player getPlayer() {
        return this.player;
    }


    @Override
    public String toString() {
        return this.score.toString();
    }
}
