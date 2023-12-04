package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PatientMedicineRepository extends CrudRepository<PatientMedicine, Long> {

    @Query("SELECT pm FROM PatientMedicine pm WHERE pm.personId = :person AND pm.medicineId = :medicine")
    PatientMedicine getPatientMedicineByPersonId(Person person, Medicine medicine);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM patient_medicine as pm WHERE pm.person_id = :personId AND pm.medicine_id = :medicineId", nativeQuery = true)
    void deletePatientMedicine(Long personId, Long medicineId);

}

