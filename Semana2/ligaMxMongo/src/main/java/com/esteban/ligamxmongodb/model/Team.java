package com.esteban.ligamxmongodb.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "teams")
public class Team {

    @Id
    private String id;

    @NotBlank(message = "Team name is required")
    private String name;

    @NotBlank(message = "Team city is required")
    private String city;

    @NotBlank(message = "Team stadium is required")
    private String stadium;

    @NotNull(message = "Founding year is required")
    @Min(value = 1800, message = "Founding year must be greater than 1800")
    @Max(value = 2100, message = "Founding year must be less than 2100")
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