package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.repositories.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AllergyService {

    @Autowired
    AllergyRepository allergyRepository;

    public Optional<Allergy> getAllergy(Long id){ return allergyRepository.findById(id);}

    public void deleteAllergy(Long personId, Long allergyId){allergyRepository.deleteAllergy(personId, allergyId);}
}
