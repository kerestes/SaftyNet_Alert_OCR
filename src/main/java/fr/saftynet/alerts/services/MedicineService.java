package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.repositories.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public Optional<Medicine> getMedicine(final Long id){return medicineRepository.findById(id);}
}
