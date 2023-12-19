package fr.saftynet.alerts.repositories.dbImpl;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.repositories.IFirestationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class FirestationRepositoryDbImpl implements IFirestationRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public Optional<Firestation> getFirestation(Long id) {
        return Optional.ofNullable(em.find(Firestation.class, id));
    }

    @Transactional
    @Override
    public Firestation saveFirestation(Firestation firestation) {
        em.persist(firestation);
        em.flush();
        return firestation;
    }

    @Override
    public List<Address> getPersonsPerFirestation(Long id) {
        return em.createQuery(DBConstants.personsPerFirestation)
                .setParameter("firestationId", id)
                .getResultList();
    }

    @Transactional
    @Override
    public void deleteFirestation(Long id) {
        Optional<Firestation> firestation = getFirestation(id);
        if(firestation.isPresent())
            em.remove(firestation.get());
    }
}
