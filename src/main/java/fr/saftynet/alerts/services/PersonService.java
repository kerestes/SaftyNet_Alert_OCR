package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person savePerson(Person person){return personRepository.save(person);}

    public void delete(final long id){personRepository.deleteById(id);}
}
