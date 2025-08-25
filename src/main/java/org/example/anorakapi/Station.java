package org.example.anorakapi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Station {
    @Id
    private String id;
    private String name;

    Station(String id, String name) throws IllegalArgumentException {
        if (name == null){
            throw new IllegalArgumentException("Station name cannot be null");
        }
        this.id = id;
        this.name = name;
    }


    public Station() {

    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
