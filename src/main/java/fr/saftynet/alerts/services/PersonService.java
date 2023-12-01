package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Optional<Person> getPerson(final Long id){return personRepository.findById(id);}

    public Person savePerson(Person person){
        if (person.getAddress() != null
                && person.getFirstName() != null && !person.getFirstName().isEmpty()
                && person.getLastName() != null && !person.getLastName().isEmpty()
                && person.getPhone() != null && !person.getPhone().isEmpty()
                && person.getEmail() != null && !person.getEmail().isEmpty())
            return personRepository.save(person);
        return null;
    }

    public Person updatePerson(Person person){
        Optional<Person> optinalPerson = getPerson(person.getId());
        if (optinalPerson.isPresent()) {
            if (person.getFirstName() == null || person.getFirstName().isEmpty())
                person.setFirstName(optinalPerson.get().getFirstName());
            if (person.getLastName() == null || person.getLastName().isEmpty())
                person.setLastName(optinalPerson.get().getLastName());
            if (person.getAddress() == null)
                person.setAddress(optinalPerson.get().getAddress());
            if (person.getPhone() == null || person.getPhone().isEmpty())
                person.setPhone(optinalPerson.get().getPhone());
            if (person.getEmail() == null || person.getEmail().isEmpty())
                person.setEmail(optinalPerson.get().getEmail());
            return savePerson(person);
        }
        return null;
    }
    public void delete(final long id){personRepository.deleteById(id);}

}
