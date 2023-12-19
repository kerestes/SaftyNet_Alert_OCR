package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;

import java.util.List;
import java.util.Optional;

public interface IFirestationRepository {
    Optional<Firestation> getFirestation(final Long id);
    Firestation saveFirestation(final Firestation firestation);
    List<Address> getPersonsPerFirestation(final Long firestationId);
    void deleteFirestation(final Long id);
}
