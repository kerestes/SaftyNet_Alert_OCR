package fr.saftynet.alerts.repositories.dbImpl;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.repositories.IAllergyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class AllergyRepositoryDbImpl implements IAllergyRepository {
    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public Optional<Allergy> getAllergy(Long id) {
        Optional<Allergy> optionalAllergy = Optional.ofNullable(em.find(Allergy.class, id));
        return optionalAllergy;
    }

    @Transactional
    @Override
    public void deleteAllergyFromPerson(Long personId, Long allergyId) {
        em.createNativeQuery(DBConstants.deleteAllergyFromPatient)
                .setParameter("personId", personId)
                .setParameter("allergyId", allergyId)
                .executeUpdate();
    }

    @Transactional
    @Override
    public Allergy saveAllergy(Allergy allergy) {
        em.persist(allergy);
        em.flush();
        return allergy;
    }
}
