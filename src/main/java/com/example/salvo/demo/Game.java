package com.example.salvo.demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    Date date ;

    public Game() {}

    public Game(Date date) {
       this.date = date;
        System.out.println(this.date);
    }

    public String toString() {
        return date.toString();
    }
}