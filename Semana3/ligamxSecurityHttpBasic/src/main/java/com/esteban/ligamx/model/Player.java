package com.esteban.ligamx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Player name is required")
    private String name;

    @NotNull(message = "Player number is required")
    @Min(value = 1, message = "Player number must be at least 1")
    @Max(value = 99, message = "Player number must be at most 99")
    private Integer number;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
