package org.example.anorakapi;

import java.util.Objects;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "station")
public class Station {
    @DocumentId
    private String stationId;
    private String name;

    Station(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Station name cannot be null");
        }
        if (Objects.equals(name, "")){
            throw new IllegalArgumentException("Station name cannot be empty");
        }
        this.name = name;
    }


    public Station() {

    }

    public String getId() {
        return stationId;
    }
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.stationId = id;
    }
}
