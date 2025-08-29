package org.example.anorakapi;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
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
    private final Firestore firestore;

    public AnorakApiService(TrainRepository trainRepository, StationRepository stationRepository, SightingRepository sightingRepository, Firestore firestore) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.sightingRepository = sightingRepository;
        this.firestore = firestore;
    }

    public List<Train> getAllTrains(){
        return trainRepository.findAll().collectList().block();
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll().collectList().block();
    }

    public Train getTrainById(String id) {
        Train train = trainRepository.findById(id).block();
        if (train == null) {
            throw new ErrorException("E404", "Train not found", HttpStatus.NOT_FOUND);
        }
        return train;
    }

    private String resolveTrainId(DocumentReference trainRef) {
        return trainRef.getId();
    }

    private String resolveStationId(DocumentReference stationRef) {
        return stationRef.getId();
    }

    private SightingDTO convertToDTO(Sighting sighting) {
        Train train = trainRepository.findById(resolveTrainId(sighting.getTrain())).block();
        Station station = stationRepository.findById(resolveStationId(sighting.getStation())).block();
        SightingDTO dto = new SightingDTO(station, train, sighting.getTimestamp());
        dto.setId(sighting.getId());
        return dto;
    }

    public List<SightingDTO> getSightingsByTrainId(String trainId) {
        List<Sighting> allSightings = sightingRepository.findAll().collectList().block();
        List<SightingDTO> dtos = new ArrayList<>();

        for (Sighting s : allSightings) {
            if (s.getTrain() != null && trainId.equals(resolveTrainId(s.getTrain()))) {
                dtos.add(convertToDTO(s));
            }
        }

        return dtos;
    }

    public List<SightingDTO> saveSightings(List<SightingDTO> sightings) {
        List<String> errors = new ArrayList<>();
        List<SightingDTO> savedSightings = new ArrayList<>();

        for (int i = 0; i < sightings.size(); i++) {
            SightingDTO dto = sightings.get(i);
            String prefix = "Sighting " + (i + 1) + ": ";

            if (dto.getTrain() == null
                    || dto.getTrain().getName() == null || dto.getTrain().getName().isEmpty()
                    || dto.getTrain().getColour() == null || dto.getTrain().getColour().isEmpty()
                    || dto.getTrain().getTrainNumber() == null || dto.getTrain().getTrainNumber().isEmpty()
                    || dto.getStation() == null
                    || dto.getStation().getName() == null || dto.getStation().getName().isEmpty()
                    || dto.getTimestamp() == null || dto.getTimestamp().isEmpty()) {
                throw new ErrorException(
                        "E001",
                        "Train, Station, and Timestamp are required and must not be empty",
                        HttpStatus.BAD_REQUEST
                );
            }

            try {
                OffsetDateTime.parse(dto.getTimestamp());

                Train train = dto.getTrain();
                if (train.getId() == null) {
                    Train existingTrain = trainRepository.findByTrainNumber(train.getTrainNumber()).block();
                    train = (existingTrain != null) ? existingTrain : trainRepository.save(train).block();
                }

                Station station = dto.getStation();
                if (station.getId() == null) {
                    Station existingStation = stationRepository.findByName(station.getName()).block();
                    station = (existingStation != null) ? existingStation : stationRepository.save(station).block();
                }

                DocumentReference trainRef = firestore.collection("train").document(train.getId());
                DocumentReference stationRef = firestore.collection("station").document(station.getId());

                Sighting sighting = new Sighting(stationRef, trainRef, dto.getTimestamp());
                sightingRepository.save(sighting).block();

                dto.setTrain(train);
                dto.setStation(station);
                dto.setId(sighting.getId());
                savedSightings.add(dto);

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
