package com.example.homework3;

public class Episode {
    String ep;
    String name;
    String air_date;
    String characters;

    public Episode(String ep, String name, String air_date, String characters){
        this.ep = ep;
        this.name = name;
        this.air_date = air_date;
        this.characters = characters;
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }
}
