package fr.saftynet.alerts.repositories.dbImpl;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.IPersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepositoryDbImpl implements IPersonRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public Optional<Person> getPerson(Long id) {
        return Optional.ofNullable(em.find(Person.class, id));
    }

    @Override
    public List<String> getEmailPerCity(String cityName) {
        return em.createQuery(DBConstants.getEmailPerCity)
                .setParameter("cityName" , "%"+cityName+"%")
                .getResultList();
    }

    @Transactional
    @Override
    public Person savePerson(Person person) {
        em.merge(person);
        return getPerson(person.getId()).get();
    }

    @Transactional
    @Override
    public void deletePerson(Long id) {
        Optional<Person> optionalPerson = getPerson(id);
        if(optionalPerson.isPresent())
            em.remove(optionalPerson.get());
    }
}
