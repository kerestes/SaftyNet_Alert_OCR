package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PatientMedicineRepository extends CrudRepository<PatientMedicine, Long> {

    @Query(DBConstants.getPatientMedicineByPersonId)
    PatientMedicine getPatientMedicineByPersonId(final Person person, final Medicine medicine);

    @Modifying
    @Transactional
    @Query(value = DBConstants.deletePatientMedicine, nativeQuery = true)
    void deletePatientMedicine(final Long personId, final Long medicineId);

}

