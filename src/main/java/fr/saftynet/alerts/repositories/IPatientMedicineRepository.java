package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;

import java.util.Optional;

public interface IPatientMedicineRepository {
    Optional<PatientMedicine> getPatientMedicineByPersonId(final Person person, final Medicine medicine);
    PatientMedicine savePatientMedicine(final PatientMedicine patientMedicine);
    void deletePatientMedicine(final Long personId, final Long medicineId);
}
