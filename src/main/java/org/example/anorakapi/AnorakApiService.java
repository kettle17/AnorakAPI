package org.example.anorakapi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return trainRepository.findAll();
    }

    public Train getTrainById(String id) {
        Optional<Train> optionalTrain = trainRepository.findById(id);
        if (!optionalTrain.isPresent()) {
            throw new ErrorException("E404", "Train not found");
        }
        return optionalTrain.get();
    }

    public List<Sighting> getSightingsByTrainId(String trainId) {
        Train train = getTrainById(trainId);
        return sightingRepository.findAllByTrain(train);
    }

    @Transactional
    public List<Sighting> saveSightings(List<Sighting> sightings) {
        List<String> errors = new ArrayList<>();
        List<Sighting> savedSightings = new ArrayList<>();

        for (Sighting sighting : sightings) {
            if (sighting.getTrain() == null || sighting.getStation() == null || sighting.getTimestamp() == null) {
                throw new ErrorException("E001", "Train, Station, and Timestamp are required");
            }

            Train train = sighting.getTrain();
            if (train.getId() == null) {
                Optional<Train> optionalTrain = trainRepository.findByTrainNumber(train.getTrainNumber());
                if (optionalTrain.isPresent()) {
                    train = optionalTrain.get();
                } else {
                    train = trainRepository.save(train);
                }
            }

            Station station = sighting.getStation();
            if (station.getId() == null) {
                Optional<Station> optionalStation = stationRepository.findByName(station.getName());
                if (optionalStation.isPresent()) {
                    station = optionalStation.get();
                } else {
                    station = stationRepository.save(station);
                }
            }

            sighting.setTrain(train);
            sighting.setStation(station);

            try {
                sightingRepository.save(sighting);
                savedSightings.add(sighting);
            } catch (Exception e) {
                errors.add("Failed to save sighting: " + sighting + " due to " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new ErrorException("E500", "Some sightings could not be saved", errors);
        }

        return savedSightings;
    }





}
