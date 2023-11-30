package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.PatientMedicine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientMedicineRepository extends CrudRepository<PatientMedicine, Long> {
}
