package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.repositories.IMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private IMedicineRepository medicineRepository;

    public Optional<Medicine> getMedicine(final Long id){return medicineRepository.getMedicine(id);}

    public Medicine saveMedicine(Medicine medicine){return medicineRepository.saveMedicine(medicine);}
}
