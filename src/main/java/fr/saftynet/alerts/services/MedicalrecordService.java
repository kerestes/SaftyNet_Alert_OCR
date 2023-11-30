package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Medicalrecord;
import fr.saftynet.alerts.repositories.MedicalrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalrecordService {
    @Autowired
    private MedicalrecordRepository medicalrecordRepository;

    public Optional<Medicalrecord> getMedicalrecord(final Long id){return medicalrecordRepository.findById(id);}

    public Medicalrecord addMedicalrecord(Medicalrecord medicalrecord){ return medicalrecordRepository.save(medicalrecord);}

    public void deleteMedicalrecord(final Long id){medicalrecordRepository.deleteById(id);}
}
