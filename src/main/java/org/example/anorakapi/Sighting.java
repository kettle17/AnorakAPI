package org.example.anorakapi;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Entity
public class Sighting {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Station station;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Train train;
    private String timestamp;

    Sighting(Station station, Train train, String timestamp) throws IllegalArgumentException {
        if (station == null || train == null | timestamp == null) {
            throw new IllegalArgumentException("Station, Train or Timestamp cannot be null");
        }
        if (timestamp.isEmpty()) {
            throw new IllegalArgumentException("Timestamp cannot be an empty string");
        }
        try {
            OffsetDateTime.parse(timestamp); // validates as ISO 8601 with offset (e.g., 2025-08-25T17:35:42.123Z)
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Timestamp is an incorrect format");
        }
        this.station = station;
        this.train = train;
        this.timestamp = timestamp;
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
}
