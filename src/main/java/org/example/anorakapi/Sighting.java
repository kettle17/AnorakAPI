package org.example.anorakapi;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Document(collectionName = "sighting")
public class Sighting {
    @DocumentId
    private String id;

    @NotNull(message = "Station reference is required")
    private DocumentReference station;

    @NotNull(message = "Train reference is required")
    private DocumentReference train;

    @NotBlank(message = "Timestamp cannot be empty")
    private String timestamp;

    Sighting(DocumentReference station, DocumentReference train, String timestamp) throws IllegalArgumentException {
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
    public DocumentReference getStation(){
        return station;
    }
    public DocumentReference getTrain(){
        return train;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTrain(DocumentReference train) {
        this.train = train;
    }
    public void setStation(DocumentReference station) {
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
