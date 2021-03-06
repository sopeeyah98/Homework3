package com.example.homework3;

public class Location {
    String name;
    String type;
    String dimension;

    public Location(String name, String type, String dimension){
        this.name = name;
        this.type = type;
        this.dimension = dimension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDimension() {
        return dimension;
    }
}
