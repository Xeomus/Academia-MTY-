package com.esteban.ligamxmongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teams")
public class Team {

    @Id
    private String id;

    private String name;
    private String city;
    private String stadium;
    private Integer foundingYear;

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
}