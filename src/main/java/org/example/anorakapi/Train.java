package org.example.anorakapi;

import java.util.Objects;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "train")
public class Train {
    @DocumentId
    private String id;

    private String name;
    private String colour;
    private String trainNumber;

    Train(String name, String colour, String trainNumber) throws IllegalArgumentException {
        if (name == null || colour == null || trainNumber == null) {
            throw new IllegalArgumentException("Train name, colour, or number cannot be null");
        }
        if (name.length() < 2 || name.length() > 100){
            throw new IllegalArgumentException("Train name must be between 2 and 100 characters");
        }
        if (Objects.equals(colour, "") || Objects.equals(trainNumber, "")){
            throw new IllegalArgumentException("Train colour or number cannot be empty");
        }

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
