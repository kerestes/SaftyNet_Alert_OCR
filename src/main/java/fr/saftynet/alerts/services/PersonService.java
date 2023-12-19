package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private IPersonRepository personRepository;

    public Optional<Person> getPerson(final Long id){return personRepository.getPerson(id);}

    public List<String> getEmailPerCity(final String cityName) {return personRepository.getEmailPerCity(cityName);}

    public Person savePerson(Person person){ return personRepository.savePerson(person);}

    public void deletePerson(final Long id){personRepository.deletePerson(id);}

}
