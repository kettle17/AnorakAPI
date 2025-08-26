package org.example.anorakapi;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;

@Service
public class AnorakApiService {
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final SightingRepository sightingRepository;

    public AnorakApiService(TrainRepository trainRepository, StationRepository stationRepository, SightingRepository sightingRepository) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.sightingRepository = sightingRepository;
    }

    public List<Train> getAllTrains(){
        return trainRepository.findAll().collectList().block();
    }

    public Train getTrainById(String id) {
        Train train = trainRepository.findById(id).block();
        if (train == null) {
            throw new ErrorException("E404", "Train not found", HttpStatus.NOT_FOUND);
        }
        return train;
    }

    public List<Sighting> getSightingsByTrainId(String trainId) {
        List<Sighting> allSightings = sightingRepository.findAll().collectList().block();
        List<Sighting> filtered = new ArrayList<>();
        for (Sighting s : allSightings) {
            if (s.getTrain() != null && trainId.equals(s.getTrain().getId())) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    public List<Sighting> saveSightings(List<Sighting> sightings) {
        List<String> errors = new ArrayList<>();
        List<Sighting> savedSightings = new ArrayList<>();

        for (int i = 0; i < sightings.size(); i++) {
            Sighting sighting = sightings.get(i);
            String prefix = "Sighting " + (i + 1) + ": ";

            if (sighting.getTrain() == null
                    || sighting.getTrain().getName() == null || sighting.getTrain().getName().isEmpty()
                    || sighting.getTrain().getColour() == null || sighting.getTrain().getColour().isEmpty()
                    || sighting.getTrain().getTrainNumber() == null || sighting.getTrain().getTrainNumber().isEmpty()
                    || sighting.getStation() == null
                    || sighting.getStation().getName() == null || sighting.getStation().getName().isEmpty()
                    || sighting.getTimestamp() == null || sighting.getTimestamp().isEmpty()) {
                throw new ErrorException(
                        "E001",
                        "Train, Station, and Timestamp are required and must not be empty",
                        HttpStatus.BAD_REQUEST
                );
            }

            try {
                OffsetDateTime.parse(sighting.getTimestamp());

                Train train = sighting.getTrain();
                if (train.getId() == null) {
                    Train existingTrain = trainRepository.findByTrainNumber(train.getTrainNumber()).block();
                    train = (existingTrain != null) ? existingTrain : trainRepository.save(train).block();
                }

                Station station = sighting.getStation();
                if (station.getId() == null) {
                    Station existingStation = stationRepository.findByName(station.getName()).block();
                    station = (existingStation != null) ? existingStation : stationRepository.save(station).block();
                }

                sighting.setTrain(train);
                sighting.setStation(station);

                sightingRepository.save(sighting).block();
                savedSightings.add(sighting);

            } catch (Exception e) {
                errors.add(prefix + "Failed to save: " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new ErrorException(
                    "E500",
                    "Some sightings could not be saved",
                    errors,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return savedSightings;
    }



}
