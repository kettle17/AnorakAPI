package org.example.anorakapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Station {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
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
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
