package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirestationRepository extends CrudRepository<Firestation, Long> {

    @Query(DBConstants.personsPerFirestation)
    List<Address> personsPerFirestation(final Long firestationId);

}
