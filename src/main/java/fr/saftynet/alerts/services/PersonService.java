package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Optional<Person> getPerson(final Long id){return personRepository.findById(id);}

    public List<String> getEmailPerCity(final String cityName) {return personRepository.getEmailPerCity(cityName);}

    public void delete(final Long id){personRepository.deleteById(id);}

    public Person savePerson(Person person){ return personRepository.save(person);}

}
