package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.repositories.IAllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AllergyService {

    @Autowired
    IAllergyRepository allergyRepository;

    public Optional<Allergy> getAllergy(final Long id){ return allergyRepository.getAllergy(id);}

    public void deleteAllergyFromPerson(final Long personId, final Long allergyId){allergyRepository.deleteAllergyFromPerson(personId, allergyId);}

    public Allergy saveAllergy(Allergy allergy){return allergyRepository.saveAllergy(allergy);}
}
