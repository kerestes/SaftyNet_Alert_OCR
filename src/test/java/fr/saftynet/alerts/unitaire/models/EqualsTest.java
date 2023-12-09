package fr.saftynet.alerts.unitaire.models;

import fr.saftynet.alerts.models.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EqualsTest {
    @Test
    public void equalsAddressTest(){
        Address address = new Address();
        Address address1 = new Address();
        Address address2 = new Address();

        address.setAddress("456 street New York");
        address1.setAddress("456 street New York");
        address2.setAddress("456 street New Jersey");

        address.setId(1L);
        address1.setId(2L);
        address2.setId(3L);

        address.setCity("New York");
        address1.setCity("New York");
        address2.setCity("New Jersey");

        address.setZip("12345");
        address1.setZip("12345");
        address2.setZip("23456");

        assertEquals(address, address1);
        assertEquals(address1, address);
        assertNotEquals(address, address2);
        assertNotEquals(address2, address1);
    }

    @Test
    public void allergyEqualsTest(){
        Allergy allergy = new Allergy();
        Allergy allergy1 = new Allergy();
        Allergy allergy2 = new Allergy();

        allergy.setId(1L);
        allergy1.setId(2L);
        allergy2.setId(3L);

        allergy.setName("peanut");
        allergy1.setName("peanut");
        allergy2.setName("shellfish");

        assertEquals(allergy, allergy1);
        assertEquals(allergy1, allergy);
        assertNotEquals(allergy, allergy2);
        assertNotEquals(allergy2, allergy1);
    }

    @Test
    public void firestationEqualsTest(){
        Firestation firestation = new Firestation();
        Firestation firestation1 = new Firestation();
        Firestation firestation2 = new Firestation();

        firestation.setId(1L);
        firestation1.setId(2L);
        firestation2.setId(3L);

        firestation.setName("Firestation 1");
        firestation1.setName("Firestation 1");
        firestation2.setName("Firestation 2");

        assertEquals(firestation, firestation1);
        assertEquals(firestation1, firestation);
        assertNotEquals(firestation, firestation2);
        assertNotEquals(firestation2, firestation1);
    }

    @Test
    public void medicineEqualsTest(){

        List<Medicine> medicines = createMedicines();

        assertEquals(medicines.get(0), medicines.get(1));
        assertEquals(medicines.get(1), medicines.get(0));
        assertNotEquals(medicines.get(0), medicines.get(2));
        assertNotEquals(medicines.get(2), medicines.get(1));
    }

    @Test
    public void patientMedicineEqualsTest(){
        List<Medicine> medicines = createMedicines();
        List<Person> persons = createPersons();

        PatientMedicine patientMedicine = new PatientMedicine();
        PatientMedicine patientMedicine1 = new PatientMedicine();
        PatientMedicine patientMedicine2 = new PatientMedicine();

        patientMedicine.setPersonId(persons.get(0));
        patientMedicine1.setPersonId(persons.get(1));
        patientMedicine2.setPersonId(persons.get(2));

        patientMedicine.setMedicineId(medicines.get(0));
        patientMedicine1.setMedicineId(medicines.get(1));
        patientMedicine2.setMedicineId(medicines.get(2));

        assertEquals(patientMedicine,  patientMedicine1);
        assertEquals(patientMedicine1, patientMedicine);
        assertNotEquals(patientMedicine, patientMedicine2);
        assertNotEquals(patientMedicine2, patientMedicine1);
    }

    @Test
    public void personEqualsTest(){
        List<Person> persons = createPersons();

        assertEquals(persons.get(0), persons.get(1));
        assertEquals(persons.get(1), persons.get(0));
        assertNotEquals(persons.get(0), persons.get(2));
        assertNotEquals(persons.get(2), persons.get(1));
    }

    private List<Medicine> createMedicines(){
        Medicine medicine = new Medicine();
        Medicine medicine1 = new Medicine();
        Medicine medicine2 = new Medicine();

        medicine.setId(1L);
        medicine1.setId(2L);
        medicine2.setId(3L);

        medicine.setName("Doliprane");
        medicine1.setName("Doliprane");
        medicine2.setName("Doliprane");

        medicine.setDosage_mg(500);
        medicine1.setDosage_mg(500);
        medicine2.setDosage_mg(1000);

        return Arrays.asList(medicine, medicine1, medicine2);
    }

    private List<Person> createPersons(){
        Person person = new Person();
        Person person1 = new Person();
        Person person2 = new Person();

        person.setId(1L);
        person1.setId(2L);
        person2.setId(3L);

        person.setFirstName("Alexandre");
        person1.setFirstName("Alexandre");
        person2.setFirstName("Andre");

        person.setLastName("KERESTES");
        person1.setLastName("KERESTES");
        person2.setLastName("KERESTES");

        person.setPhone("0123456789");
        person1.setPhone("2345678901");
        person2.setPhone("1234567890");

        return Arrays.asList(person, person1, person2);
    }
}
