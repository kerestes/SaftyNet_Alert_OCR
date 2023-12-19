package fr.saftynet.alerts.repositories.dbImpl;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.IPatientMedicineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class PatientMedicineRepositoryDbImpl implements IPatientMedicineRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public Optional<PatientMedicine> getPatientMedicineByPersonId(Person person, Medicine medicine) {
        List<PatientMedicine> patientMedicineList = em.createQuery(DBConstants.getPatientMedicineByPersonId)
                .setParameter("person", person)
                .setParameter("medicine", medicine)
                .getResultList();
        if (!patientMedicineList.isEmpty())
            return Optional.ofNullable(patientMedicineList.get(0));
        return Optional.empty();
    }

    @Transactional
    @Override
    public PatientMedicine savePatientMedicine(PatientMedicine patientMedicine) {
        em.persist(patientMedicine);
        em.flush();
        return patientMedicine;
    }

    @Transactional
    @Override
    public void deletePatientMedicine(Long personId, Long medicineId) {
        em.createNativeQuery(DBConstants.deletePatientMedicine)
                .setParameter("personId", personId)
                .setParameter("medicineId", medicineId)
                .executeUpdate();
    }
}
