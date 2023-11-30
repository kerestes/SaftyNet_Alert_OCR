package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Medicalrecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalrecordRepository extends CrudRepository<Medicalrecord, Long> {
}
