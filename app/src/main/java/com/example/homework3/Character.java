package com.example.homework3;

import java.util.List;

public class Character {
    String name;
    String status;
    String species;
    String gender;
    String origin;
    String location;
    String image_url;
    List<Integer> episodes;

    public Character(String name, String status, String species, String gender, String origin, String location, String image_url, List<Integer> episodes) {
        this.name = name;
        this.status = status;
        this.species = species;
        this.gender = gender;
        this.origin = origin;
        this.location = location;
        this.image_url = image_url;
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<Integer> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Integer> episodes) {
        this.episodes = episodes;
    }
}
