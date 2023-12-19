package fr.saftynet.alerts.repositories.dbImpl;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.repositories.IMedicineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class MedicineRepositoryDbImpl implements IMedicineRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public Optional<Medicine> getMedicine(Long id) {
        return Optional.ofNullable(em.find(Medicine.class, id));
    }

    @Transactional
    @Override
    public Medicine saveMedicine(Medicine medicine) {
        em.persist(medicine);
        em.flush();
        return medicine;
    }
}
