package org.example.anorakapi;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SightingRepository extends FirestoreReactiveRepository<Sighting> {
}
