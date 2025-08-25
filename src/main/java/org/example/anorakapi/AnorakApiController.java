package org.example.anorakapi;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("")
public class AnorakApiController {
    private final AnorakApiService anorakApiService;

    public AnorakApiController(AnorakApiService anorakApiService) {
        this.anorakApiService = anorakApiService;
    }

    @GetMapping("/train")
    public List<Train> getListTrains(){
        return anorakApiService.getAllTrains();
    }

    @GetMapping("/train/{id}")
    public Train getTrainById(@PathVariable String id){
        return anorakApiService.getTrainById(id);
    }

    @GetMapping("/train/{id}/sightings")
    public List<Sighting> getSightings(@PathVariable String id){
        return anorakApiService.getSightingsByTrainId(id);
    }

    @PostMapping("/sightings")
    public List<Sighting> saveSightings(@RequestBody List<Sighting> sightings) {
        return anorakApiService.saveSightings(sightings);
    }
}
