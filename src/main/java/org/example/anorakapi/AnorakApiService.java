package org.example.anorakapi;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Train train = getTrainById(trainId);
        return sightingRepository.findAllByTrain(train).collectList().block();
    }


    public List<Sighting> saveSightings(List<Sighting> sightings) {
        List<String> errors = new ArrayList<>();
        List<Sighting> savedSightings = new ArrayList<>();

        for (Sighting sighting : sightings) {
            if (sighting.getTrain() == null || sighting.getStation() == null || sighting.getTimestamp() == null) {
                throw new ErrorException("E001", "Train, Station, and Timestamp are required", HttpStatus.BAD_REQUEST);
            }

            Train train = sighting.getTrain();
            if (train.getId() == null) {
                Train existingTrain = trainRepository.findByTrainNumber(train.getTrainNumber()).block();
                if (existingTrain != null) {
                    train = existingTrain;
                } else {
                    train = trainRepository.save(train).block();
                }
            }

            Station station = sighting.getStation();
            if (station.getId() == null) {
                Station existingStation = stationRepository.findByName(station.getName()).block();
                if (existingStation != null) {
                    station = existingStation;
                } else {
                    station = stationRepository.save(station).block();
                }
            }

            sighting.setTrain(train);
            sighting.setStation(station);

            try {
                sightingRepository.save(sighting).block();
                savedSightings.add(sighting);
            } catch (Exception e) {
                errors.add("Failed to save sighting: " + sighting + " due to " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new ErrorException("E500", "Some sightings could not be saved", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return savedSightings;
    }

}
