package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Medicine;

import java.util.Optional;

public interface IMedicineRepository {
    Optional<Medicine> getMedicine(final Long id);
    Medicine saveMedicine(Medicine medicine);
}
