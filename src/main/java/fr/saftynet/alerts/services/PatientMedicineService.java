package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.PatientMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientMedicineService {

    @Autowired
    private PatientMedicineRepository patientMedicineRepository;

    public PatientMedicine getPatientMedicineByPersonId(final Person person, final Medicine medicine){return patientMedicineRepository.getPatientMedicineByPersonId(person, medicine);}

    public PatientMedicine savePatientMedicine(final PatientMedicine patientMedicine){return patientMedicineRepository.save(patientMedicine);}

    public void deletePatientMedicine(final Long personId, final Long medicineId){patientMedicineRepository.deletePatientMedicine(personId, medicineId);}
}
