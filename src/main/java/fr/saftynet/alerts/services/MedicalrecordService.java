package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Medicalrecord;
import fr.saftynet.alerts.repositories.MedicalrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MedicalrecordService {
    @Autowired
    private MedicalrecordRepository medicalrecordRepository;

    public Optional<Medicalrecord> getMedicalrecord(final Long id){return medicalrecordRepository.findById(id);}

    public Medicalrecord addMedicalrecord(Medicalrecord medicalrecord){
        if(medicalrecord.getFirstName() != null && !medicalrecord.getFirstName().isEmpty()
            && medicalrecord.getLastName() != null && !medicalrecord.getLastName().isEmpty()
            && medicalrecord.getBirthday() != null && medicalrecord.getAllergies() != null
            && medicalrecord.getMedicines() != null)
            return medicalrecordRepository.save(medicalrecord);
        return null;
    }

    public Medicalrecord updateMedicalrecord(Medicalrecord medicalrecord, Medicalrecord realMedicalrecord){
        if(medicalrecord.getFirstName() == null || medicalrecord.getFirstName().isEmpty())
            medicalrecord.setFirstName(realMedicalrecord.getFirstName());
        if(medicalrecord.getLastName() == null || medicalrecord.getLastName().isEmpty())
            medicalrecord.setLastName(realMedicalrecord.getLastName());
        if(medicalrecord.getBirthday() == null)
            medicalrecord.setBirthday(realMedicalrecord.getBirthday());
        medicalrecord.setMedicines(realMedicalrecord.getMedicines());
        medicalrecord.setAllergies(realMedicalrecord.getAllergies());
        return addMedicalrecord(medicalrecord);
    }

    public void deleteMedicalrecord(final Long id){medicalrecordRepository.deleteById(id);}
}
