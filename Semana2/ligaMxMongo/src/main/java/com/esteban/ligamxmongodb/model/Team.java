package com.esteban.ligamxmongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "teams")
public class Team {

    @Id
    private String id;

    private String name;
    private String city;
    private String stadium;
    private Integer foundingYear;
    private List<Player> players = new ArrayList<>();

    public Team() {
    }

    public Team(String name, String city, String stadium, Integer foundingYear) {
        this.name = name;
        this.city = city;
        this.stadium = stadium;
        this.foundingYear = foundingYear;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public Integer getFoundingYear() {
        return foundingYear;
    }

    public void setFoundingYear(Integer foundingYear) {
        this.foundingYear = foundingYear;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}