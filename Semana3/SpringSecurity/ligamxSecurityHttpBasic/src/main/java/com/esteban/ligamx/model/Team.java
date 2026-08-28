package com.esteban.ligamx.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Team name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Stadium is required")
    private String stadium;

    @Min(value = 1800, message = "Founding year must be greater than or equal to 1800")
    private Integer foundingYear;

    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();

    public Team() {}

    public Team(String name, String city, String stadium, Integer foundingYear) {
        this.name = name;
        this.city = city;
        this.stadium = stadium;
        this.foundingYear = foundingYear;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
