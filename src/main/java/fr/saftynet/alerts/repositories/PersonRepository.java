package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query(DBConstants.getEmailPerCity)
    List<String> getEmailPerCity(final String cityName);
}
