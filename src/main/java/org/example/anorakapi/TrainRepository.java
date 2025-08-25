package org.example.anorakapi;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, String> {
}
