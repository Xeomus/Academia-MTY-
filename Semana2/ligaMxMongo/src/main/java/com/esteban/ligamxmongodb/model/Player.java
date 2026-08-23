package com.esteban.ligamxmongodb.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Player {

    private String id;

    @NotBlank(message = "Player name is required")
    private String name;

    @NotNull(message = "Player number is required")
    @Min(value = 1, message = "Player number must be at least 1")
    @Max(value = 99, message = "Player number must be at most 99")
    private Integer number;

    @NotBlank(message = "Player position is required")
    private String position;

    @NotBlank(message = "Player nationality is required")
    private String nationality;

    public Player() {
    }

    public Player(String name, Integer number, String position, String nationality) {
        this.name = name;
        this.number = number;
        this.position = position;
        this.nationality = nationality;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}