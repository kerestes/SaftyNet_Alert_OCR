package fr.saftynet.alerts.integration;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.PatientMedicineService;
import static org.junit.jupiter.api.Assertions.*;

import fr.saftynet.alerts.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PatientMedicineServiceIT {
    @Autowired
    PatientMedicineService patientMedicineService;
    @Autowired
    PersonService personService;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void getPatientMedicineByPersonIdIT(){
        Person person = new Person();
        person.setId(1L);

        Medicine medicine = new Medicine();
        medicine.setId(3L);

        Optional<PatientMedicine> optionalPatientMedicine = patientMedicineService.getPatientMedicineByPersonId(person, medicine);

        assertTrue(optionalPatientMedicine.isPresent());
    }

    @Test
    public void getPatientMedicineByPersonIdNullReturnIT(){
        Person person = new Person();
        person.setId(1L);

        Medicine medicine = new Medicine();
        medicine.setId(10L);

        Optional<PatientMedicine> optionalPatientMedicine = patientMedicineService.getPatientMedicineByPersonId(person, medicine);

        assertTrue(optionalPatientMedicine.isEmpty());
    }

    @Test
    public void savePatientMedicineIT(){
        Person person = new Person();
        person.setId(1L);

        Medicine medicine = new Medicine();
        medicine.setId(1L);

        PatientMedicine patientMedicine = new PatientMedicine();
        patientMedicine.setQuantity(3);
        patientMedicine.setMedicineId(medicine);
        patientMedicine.setPersonId(person);

        PatientMedicine patientMedicineResult = patientMedicineService.savePatientMedicine(patientMedicine);

        assertNotNull(patientMedicineResult);
        assertEquals(3, patientMedicineResult.getQuantity());
    }

    @Test
    public void deleteMedicineIT() throws Exception {
        Optional<Person> optionalPerson = personService.getPerson(5L);
        assertTrue(optionalPerson.isPresent());
        assertEquals(1, optionalPerson.get().getMedicines().size());
        assertEquals("tetracyclaz", optionalPerson.get().getMedicines().get(0).getMedicineId().getName());

        mockMvc.perform(delete("/medicine/{personId}/{medicineId}", 5, 10))
                .andExpect( status().isOk() );

        optionalPerson = personService.getPerson(5L);
        assertTrue(optionalPerson.get().getMedicines().isEmpty());
    }
}
