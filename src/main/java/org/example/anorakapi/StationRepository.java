package org.example.anorakapi;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StationRepository extends FirestoreReactiveRepository<Station> {
    Mono<Station> findByName(String name);
}