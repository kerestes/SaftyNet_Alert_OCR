package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Person;

import java.util.List;
import java.util.Optional;

public interface IPersonRepository {
    public Optional<Person> getPerson(final Long id);
    List<String> getEmailPerCity(final String cityName);
    Person savePerson(Person person);
    void deletePerson(final Long id);
}
