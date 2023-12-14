package fr.saftynet.alerts.unitaire.SpringContext;

import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicalrecordControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    PersonService personService;
    @MockBean
    PatientMedicineService patientMedicineService;
    @MockBean
    MedicineService medicineService;
    @MockBean
    AllergyService allergyService;

    @Test
    public void addMedicineTest() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId(10L);
        medicine.setName("test name");
        medicine.setDosage_mg(100);
        when(medicineService.saveMedicine(any())).thenReturn(medicine);

        mockMvc.perform(post("/addMedicine").content("{\"name\":\"test name\", \"dosage_mg\":100}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("test name")),
                        jsonPath("$.dosage_mg", is(100))
                );
    }

    @Test
    public void addMedicineErrorSavingTest() throws Exception {
        when(medicineService.saveMedicine(any())).thenReturn(null);

        mockMvc.perform(post("/addMedicine").content("{\"name\":\"test name\", \"dosage_mg\":100}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There was an error to save medicine"))
                );
    }

    @Test
    public void addMedicineNoNameRequestTest() throws Exception {

        mockMvc.perform(post("/addMedicine").content("{ \"dosage_mg\":100}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no name or dosage_mg in the request body"))
                );
    }

    @Test
    public void addAllergyTest() throws Exception {
        Allergy allergy = new Allergy();
        allergy.setId(10L);
        allergy.setName("Allergy test");
        when(allergyService.saveAllergy(any())).thenReturn(allergy);

        mockMvc.perform(post("/addAllergy").content("{\"name\":\"Allergy test\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("Allergy test"))
                );
    }

    @Test
    public void addAllergyErrorSavingTest() throws Exception {
        when(allergyService.saveAllergy(any())).thenReturn(null);

        mockMvc.perform(post("/addAllergy").content("{\"name\":\"Allergy test\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There was an error to save medicine"))
                );
    }

    @Test
    public void addAllergyNoNameTest() throws Exception {

        mockMvc.perform(post("/addAllergy").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no name in the request body"))
                );
    }

    @Test
    public void addMedicineToPersonTest() throws Exception {
        Optional<Medicine> medicine = Optional.of(new Medicine());
        medicine.get().setName("aznol");
        medicine.get().setId(1L);
        medicine.get().setDosage_mg(60);

        when(medicineService.getMedicine(any())).thenReturn(medicine);
        when(personService.getPerson(any())).thenReturn(makePerson());
        when(patientMedicineService.getPatientMedicineByPersonId(any(), any())).thenReturn(Optional.empty());
        when(patientMedicineService.savePatientMedicine(any())).thenReturn(null);
        when(personService.getPerson(any())).thenReturn(makePerson());

        mockMvc.perform(put("/medicine/{personId}", 1).content("{\"quantity\": 2, \"medicineId\": 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.firstName", containsStringIgnoringCase("john")),
                        jsonPath("$.lastName", containsStringIgnoringCase("boyd")),
                        jsonPath("$.medicines", hasSize(3)),
                        jsonPath("$.medicines.[2].quantity", is(2)),
                        jsonPath("$.medicines.[2].medicineId.name", containsStringIgnoringCase("aznol"))
                );
    }

    @Test
    public void addMedicineToPersonInvalidPersonIdTest() throws Exception {

        mockMvc.perform(put("/medicine/{personId}", 0).content("{\"quantity\": 2, \"medicineId\": 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Person id"))
                );
    }

    @Test
    public void addMedicineToPersonNoMedicineIdTest() throws Exception {

        mockMvc.perform(put("/medicine/{personId}", 1).content("{\"quantity\": 2}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", containsStringIgnoringCase("Invalid Medicine id"))
                );
    }

    @Test
    public void addMedicineToPersonNoPersonOrMedicineTest() throws Exception {

        when(medicineService.getMedicine(any())).thenReturn(Optional.empty());
        when(personService.getPerson(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicine/{personId}", 1).content("{\"quantity\": 2, \"medicineId\": 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Person (id=1) or Medicine (id=1) does not exist"))
                );
    }

    @Test
    public void addAllergyToPersonTest() throws Exception{
        Allergy allergy = new Allergy();
        allergy.setName("aznol");
        allergy.setId(1L);

        Person person = makePerson().get();

        when(personService.getPerson(1L)).thenReturn(makePerson());
        when(allergyService.getAllergy(1L)).thenReturn(Optional.of(allergy));
        when(personService.savePerson(any())).thenReturn(makePerson().get());

        mockMvc.perform(put("/allergy/{personId}", 1).content("{\"allergyId\" : 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isOk()
                );

    }

    @Test
    public void addAllergyToPersonNoPersonIdTest() throws Exception{

        mockMvc.perform(put("/allergy/{personId}", 0).content("{\"allergyId\" : 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Person id"))
                );

    }

    @Test
    public void addAllergyToPersonNoAllergyIdTest() throws Exception{

        mockMvc.perform(put("/allergy/{personId}", 1).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Allergy id"))
                );

    }

    @Test
    public void addAllergyToPersonNoPersonOrAllergyTest() throws Exception{

        when(personService.getPerson(1L)).thenReturn(Optional.empty());
        when(allergyService.getAllergy(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/allergy/{personId}", 1).content("{\"allergyId\" : 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Person (id=1) or Allergy (id=1) does not exist"))
                );

    }

    @Test
    public void deleteMedicineNoPersonIdTest() throws Exception {
        mockMvc.perform(delete("/medicine/{personId}/{medicineId}", 0, 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Person Id"))
                );
    }

    @Test
    public void deleteMedicineNoMedicineIdTest() throws Exception {
        mockMvc.perform(delete("/medicine/{personId}/{medicineId}", 1, 0))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Medicine Id"))
                );
    }

    @Test
    public void deleteAllergieNoPersonIdTest() throws Exception {
        mockMvc.perform(delete("/allergy/{personId}/{medicineId}", 0, 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Person Id"))
                );
    }

    @Test
    public void deleteAllergieNoMedicineIdTest() throws Exception {
        mockMvc.perform(delete("/allergy/{personId}/{medicineId}", 1, 0))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid Allergy Id"))
                );
    }

    private Optional<Person> makePerson() {
        Optional<Person> optionalPerson = Optional.of(new Person());
        Calendar calendar = Calendar.getInstance();

        Allergy allergy = new Allergy();
        allergy.setId(3L);
        allergy.setName("nillacilan");

        Allergy allergy2 = new Allergy();
        allergy2.setId(1L);
        allergy2.setName("aznol");

        Medicine medicine1 = new Medicine();
        medicine1.setId(3L);
        medicine1.setName("aznol");
        medicine1.setDosage_mg(350);

        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setMedicineId(medicine1);
        patientMedicine1.setQuantity(1);

        Medicine medicine2 = new Medicine();
        medicine2.setId(5L);
        medicine2.setName("hydrapermazol");
        medicine2.setDosage_mg(100);

        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setMedicineId(medicine2);
        patientMedicine2.setQuantity(1);

        Medicine medicine3 = new Medicine();
        medicine3.setId(1L);
        medicine3.setName("aznol");
        medicine3.setDosage_mg(60);

        PatientMedicine patientMedicine3 = new PatientMedicine();
        patientMedicine3.setMedicineId(medicine3);
        patientMedicine3.setQuantity(2);

        optionalPerson.get().setId(1L);
        optionalPerson.get().setFirstName("John");
        optionalPerson.get().setLastName("BOYD");
        calendar.set(1984, 3, 5);
        optionalPerson.get().setBirthday(calendar.getTime());
        optionalPerson.get().setPhone("8418746512");
        optionalPerson.get().setEmail("jaboyd@email.com");
        optionalPerson.get().setAllergies(Arrays.asList(allergy, allergy2));
        optionalPerson.get().setMedicines(Arrays.asList(patientMedicine1, patientMedicine2, patientMedicine3));

        return optionalPerson;
    }
}


