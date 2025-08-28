package org.example.anorakapi;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Document(collectionName = "sighting")
public class Sighting {
    @DocumentId
    private String id;
    @Valid
    private Station station;

    @Valid
    private Train train;

    @NotBlank(message = "Timestamp cannot be empty")
    private String timestamp;

    Sighting(Station station, Train train, String timestamp) throws IllegalArgumentException {
        if (station == null || train == null || timestamp == null) {
            throw new IllegalArgumentException("Station, Train or Timestamp cannot be null");
        }
        if (timestamp.isEmpty()) {
            throw new IllegalArgumentException("Timestamp cannot be an empty string");
        }
        try {
            OffsetDateTime.parse(timestamp); // validates ISO 8601 with offset
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Timestamp is an incorrect format", e);
        }
        this.station = station;
        this.train = train;
        this.timestamp = timestamp; // still stored as string
    }

    public Sighting() {

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Station getStation(){
        return station;
    }
    public Train getTrain(){
        return train;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTrain(Train train) {
        this.train = train;
    }
    public void setStation(Station station) {
        this.station = station;
    }

    public void setTimestamp(String timestamp) throws IllegalArgumentException {
        try {
            OffsetDateTime.parse(timestamp);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Timestamp is an incorrect format", e);
        }
        this.timestamp = timestamp;
    }
}
