package fr.saftynet.alerts.unitaire;

import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MedicalRecordUtilityTest {

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

        Assertions.assertTrue(resultMedicinePersonId1);
        Assertions.assertTrue(resultMedicinePersonId2);
        Assertions.assertTrue(resultMedicinePersonId3);
        Assertions.assertFalse(resultMedicinePersonId4);
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

        Assertions.assertEquals("KERESTES", patientMedicine.getPersonId().getLastName());
        Assertions.assertEquals(1, patientMedicine.getQuantity());
        Assertions.assertEquals("Doliprane", patientMedicine.getMedicineId().getName());
    }

    @Test
    public void addAllergieTest(){
        Allergy allergy = new Allergy();
        allergy.setId(1L);
        allergy.setName("peanut");
        Person person = new Person();

        person = MedicalRecordUtility.addAllergie(person, allergy);

        Assertions.assertTrue(person.getAllergies().contains(allergy));
        Assertions.assertEquals("peanut", person.getAllergies().get(0).getName());
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

        Assertions.assertTrue(person.getMedicines().contains(patientMedicine));
        Assertions.assertEquals(1L, person.getMedicines().get(0).getMedicineId().getId());

        person = MedicalRecordUtility.setNullMecialRecord(person);

        Assertions.assertNull(person.getAllergies());
        Assertions.assertNull(person.getMedicines());
    }
}
