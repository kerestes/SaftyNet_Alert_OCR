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
    @MockBean
    AddressService addressService;

    @Test
    public void getPersonByAddressWithMedicalRecordsTest() throws Exception {
        when(addressService.getAddressByName(any())).thenReturn(makeAddress());
        mockMvc.perform(get("/fire?address={address}", "112 Steppes"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.address", is("112 Steppes Pl")),
                        jsonPath("$.firestation.name", is("Firestation 4")),
                        jsonPath("$.persons", hasSize(3)),
                        jsonPath("$.persons.[0].lastName", containsStringIgnoringCase("Cooper")),
                        jsonPath("$.persons.[2].allergies.[0].name", is("nillacilan"))
                );
    }

    @Test
    public void addMedicineTest() throws Exception {
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
    public void addMedicineIdZeroTest() throws Exception {

        when(medicineService.getMedicine(any())).thenReturn(Optional.empty());
        when(personService.getPerson(any())).thenReturn(makePerson());

        mockMvc.perform(put("/medicine/{personId}", 1).content("{\"quantity\": 2, \"medicineId\" : 0}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void addAllergyTest() throws Exception{
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
    public void addAllergyAllergyEmptyTest() throws Exception{
        Optional<Allergy> allergy = Optional.of(new Allergy());
        allergy.get().setName("aznol");

        Person person = makePerson().get();

        when(personService.getPerson(1L)).thenReturn(makePerson());
        when(allergyService.getAllergy(1L)).thenReturn(Optional.empty());
        when(personService.savePerson(any())).thenReturn(makePerson().get());

        mockMvc.perform(put("/allergy/{personId}", 1).content("{\"allergyId\" : 1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );

    }

    private Optional<Address> makeAddress() {
        Optional<Address> optionalAddress = Optional.of(new Address());
        Calendar calendar = Calendar.getInstance();

        Firestation firestation = new Firestation();
        firestation.setName("Firestation 4");
        firestation.setId(4L);

        Medicine medicine1 = new Medicine();
        medicine1.setId(5L);
        medicine1.setName("hydrapermazol");
        medicine1.setDosage_mg(100);

        Medicine medicine2 = new Medicine();
        medicine2.setId(4L);
        medicine2.setName("dodoxadin");
        medicine2.setDosage_mg(30);

        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setQuantity(3);
        patientMedicine1.setMedicineId(medicine1);

        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setQuantity(1);
        patientMedicine2.setMedicineId(medicine2);

        Allergy allergy1 = new Allergy();
        allergy1.setId(5L);
        allergy1.setName("shellfish");

        Person person1 = new Person();
        person1.setId(10L);
        person1.setFirstName("Tony");
        person1.setLastName("COOPER");
        calendar.set(1994, 3, 6);
        person1.setBirthday(calendar.getTime());
        person1.setPhone("8418746874");
        person1.setEmail("tcoop@ymail.com");
        person1.setMedicines(Arrays.asList(patientMedicine1, patientMedicine2));
        person1.setAllergies(Arrays.asList(allergy1));

        Person person2 = new Person();
        person2.setId(17L);
        person2.setFirstName("Ron");
        person2.setLastName("PETERS");
        calendar.set(1965, 4, 6);
        person2.setBirthday(calendar.getTime());
        person2.setPhone("8418748888");
        person2.setEmail("jpeter@email.com");

        Allergy allergy2 = new Allergy();
        allergy2.setId(3L);
        allergy2.setName("nillacilan");

        Medicine medicine3 = new Medicine();
        medicine3.setId(2L);
        medicine3.setName("aznol");
        medicine3.setDosage_mg(200);

        PatientMedicine patientMedicine3 = new PatientMedicine();
        patientMedicine3.setQuantity(1);
        patientMedicine3.setMedicineId(medicine3);

        Person person3 = new Person();
        person3.setId(18L);
        person3.setFirstName("Allison");
        person3.setLastName("BOYD");
        calendar.set(1965, 3, 15);
        person3.setBirthday(calendar.getTime());
        person3.setPhone("8418749888");
        person3.setEmail("aly@imail.com");
        person3.setAllergies(Arrays.asList(allergy2));
        person3.setMedicines(Arrays.asList(patientMedicine3));

        optionalAddress.get().setId(6L);
        optionalAddress.get().setAddress("112 Steppes Pl");
        optionalAddress.get().setCity("Culver");
        optionalAddress.get().setZip("97451");
        optionalAddress.get().setFirestation(firestation);
        optionalAddress.get().setPersons(Arrays.asList(person1, person2, person3));
        return optionalAddress;
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


