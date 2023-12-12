package fr.saftynet.alerts.unitaire.utilities;

import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import nl.altindag.log.LogCaptor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MedicalRecordUtilityTest {

    LogCaptor logCaptor = LogCaptor.forClass(MedicalRecordUtility.class);

    @Test
    public void createMedicalRecordForNewPersonTest(){
        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setMedicineId(new Medicine());
        patientMedicine1.getMedicineId().setId(10L);
        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setMedicineId(new Medicine());
        patientMedicine2.getMedicineId().setId(20L);
        PatientMedicine patientMedicine3 = new PatientMedicine();
        patientMedicine3.setMedicineId(new Medicine());
        patientMedicine3.getMedicineId().setId(30L);

        Person person = new Person();
        person.setId(1L);
        person.setMedicines(MedicalRecordUtility.createMedicalRecordForNewPerson(Arrays.asList(patientMedicine1, patientMedicine2, patientMedicine3), person));

        boolean resultMedicinePersonId1 = person.getMedicines().stream().anyMatch(patientMedicine -> patientMedicine.getMedicineId().getId() == 10L);
        boolean resultMedicinePersonId2 = person.getMedicines().stream().anyMatch(patientMedicine -> patientMedicine.getMedicineId().getId() == 20L);
        boolean resultMedicinePersonId3 = person.getMedicines().stream().anyMatch(patientMedicine -> patientMedicine.getMedicineId().getId() == 30L);
        boolean resultMedicinePersonId4 = person.getMedicines().stream().anyMatch(patientMedicine -> patientMedicine.getMedicineId().getId() == 1L);

        assertTrue(resultMedicinePersonId1);
        assertTrue(resultMedicinePersonId2);
        assertTrue(resultMedicinePersonId3);
        assertFalse(resultMedicinePersonId4);
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Creating a medical record to"));
    }

    @Test
    public void addMedicineTest(){
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Doliprane");
        Person person = new Person();
        person.setFirstName("Alexandre");
        person.setLastName("KERESTES");
        int quantity = 1;

        PatientMedicine patientMedicine = MedicalRecordUtility.addMedicine(quantity, person, medicine);

        assertEquals("KERESTES", patientMedicine.getPersonId().getLastName());
        assertEquals(1, patientMedicine.getQuantity());
        assertEquals("Doliprane", patientMedicine.getMedicineId().getName());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Connecting person"));
    }

    @Test
    public void addAllergieTest(){
        Allergy allergy = new Allergy();
        allergy.setId(1L);
        allergy.setName("peanut");
        Person person = new Person();

        person = MedicalRecordUtility.addAllergy(person, allergy);

        assertTrue(person.getAllergies().contains(allergy));
        assertEquals("peanut", person.getAllergies().get(0).getName());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Adding allergy"));
    }

    @Test
    public void setNullMecialRecordTest(){
        Person person = new Person();

        Allergy allergy = new Allergy();
        allergy.setId(1L);
        allergy.setName("peanut");

        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Doliprane");

        PatientMedicine patientMedicine = new PatientMedicine();
        patientMedicine.setMedicineId(medicine);

        person.setAllergies(Arrays.asList(allergy));
        person.setMedicines(Arrays.asList(patientMedicine));

        assertTrue(person.getMedicines().contains(patientMedicine));
        assertEquals(1L, person.getMedicines().get(0).getMedicineId().getId());

        person = MedicalRecordUtility.setNullMecialRecord(person);

        assertNull(person.getAllergies());
        assertNull(person.getMedicines());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Setting allergies and medicines to null"));
    }
}
