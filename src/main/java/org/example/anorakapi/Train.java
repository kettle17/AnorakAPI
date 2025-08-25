package org.example.anorakapi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Train {
    @Id
    private String id;
    private String name;
    private String colour;
    private String trainNumber;

    Train(String id, String name, String colour, String trainNumber) throws IllegalArgumentException {
        if (name == null || colour == null || trainNumber == null){
            throw new IllegalArgumentException("Train name, colour, or train number cannot be null");
        }
        if (name.length() < 2 || name.length() > 100){
            throw new IllegalArgumentException("Train name must be between 2 and 100 characters");
        }

        this.id = id;
        this.name = name;
        this.colour = colour;
        this.trainNumber = trainNumber;
    }


    public Train() {

    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getColour() {
        return colour;
    }
    public String getTrainNumber() {
        return trainNumber;
    }

    public void setId(String id) {
        this.id = id;
    }
}
