package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Allergy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AllergyRepository extends CrudRepository<Allergy, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM patient_allergy where person_id = :personId AND allergy_id = :allergyId", nativeQuery = true)
    void deleteAllergy(Long personId, Long allergyId);
}
