package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Firestation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirestationRepository extends CrudRepository<Firestation, Long> {
}
