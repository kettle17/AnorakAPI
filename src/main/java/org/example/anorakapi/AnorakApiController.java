package org.example.anorakapi;

import org.apache.coyote.Response;
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
    public Map<String, List<Sighting>> getSightings(@PathVariable String id){
        List<Sighting> sightings = anorakApiService.getSightingsByTrainId(id);
        return Map.of("sightings", sightings);
    }

    @PostMapping("/sightings")
    public Map<String, List<Sighting>> saveSightings(@RequestBody List<Sighting> sightings) {
        List<Sighting> returnedSightings = anorakApiService.saveSightings(sightings);
        return Map.of("sightings", returnedSightings);
    }
}
