package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.repositories.PatientMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientMedicineService {

    @Autowired
    private PatientMedicineRepository patientMedicineRepository;

    public PatientMedicine savePatientMedicine(PatientMedicine patientMedicine){return patientMedicineRepository.save(patientMedicine);}
}
