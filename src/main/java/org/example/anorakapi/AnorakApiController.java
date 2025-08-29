package org.example.anorakapi;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class AnorakApiController {
    private final AnorakApiService anorakApiService;

    public AnorakApiController(AnorakApiService anorakApiService) {
        this.anorakApiService = anorakApiService;
    }

    @GetMapping("/train")
    public Map<String, List<Train>> getListTrains(){
        List<Train> trains = anorakApiService.getAllTrains();
        return Map.of("trains", trains);
    }

    @GetMapping("/train/{id}")
    public Train getTrainById(@PathVariable String id){
        return anorakApiService.getTrainById(id);
    }

    @GetMapping("/train/{id}/sightings")
    public Map<String, List<SightingDTO>> getSightings(@PathVariable String id){
        List<SightingDTO> sightings = anorakApiService.getSightingsByTrainId(id);
        return Map.of("sightings", sightings);
    }

    @GetMapping("/station")
    public Map<String, List<Station>> getListStations(){
        List<Station> stations = anorakApiService.getAllStations();
        return Map.of("stations", stations);
    }

    @PostMapping("/sightings")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, List<SightingDTO>> saveSightings(@Valid @RequestBody List<SightingDTO> sightings) {
        List<SightingDTO> returnedSightings = anorakApiService.saveSightings(sightings);
        return Map.of("sightings", returnedSightings);
    }
}
