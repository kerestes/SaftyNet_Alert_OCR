package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Allergy;

import java.util.Optional;

public interface IAllergyRepository {
    Optional<Allergy> getAllergy(final Long id);
    void deleteAllergyFromPerson(final Long personId, final Long allergyId);
    Allergy saveAllergy(Allergy allergy);
}
