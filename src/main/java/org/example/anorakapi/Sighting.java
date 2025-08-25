package org.example.anorakapi;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Sighting {
    @Id
    private String id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Station station;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Train train;
    private String timestamp;

    Sighting(String id, Station station, Train train, String timestamp) throws IllegalArgumentException {
        if (station == null || train == null || timestamp == null) {
            throw new IllegalArgumentException("Station, Train or timestamp cannot be null");
        }
        this.id = id;
        this.station = station;
        this.train = train;
        this.timestamp = timestamp; //validate date as ISO formatted?
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
