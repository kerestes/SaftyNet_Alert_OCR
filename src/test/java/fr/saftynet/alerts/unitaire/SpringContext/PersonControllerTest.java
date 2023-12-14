package fr.saftynet.alerts.unitaire.SpringContext;

import fr.saftynet.alerts.controllers.PersonController;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.AddressUtility;
import nl.altindag.log.LogCaptor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PersonService personService;
    @MockBean
    private PatientMedicineService patientMedicineService;
    @MockBean
    private AddressService addressService;



    @Test
    public void createNewPersonTest() throws Exception{

        when(personService.savePerson(any())).thenReturn(newPerson());
        when(patientMedicineService.savePatientMedicine(any())).thenReturn(null);
        when(personService.getPerson(any())).thenReturn(Optional.of(newPerson()));

        mockMvc.perform(post("/person").content("{" +
                        "    \"firstName\":\"Alexandre\"," +
                        "    \"lastName\":\"Kerestes\"," +
                        "    \"birthday\":\"1987-11-24\"," +
                        "    \"phone\":\"1234567890\"," +
                        "    \"email\":\"alexandrekerestes@exemplo.fr\"," +
                        "    \"address\":{\"id\":1}," +
                        "    \"allergies\":[" +
                        "        {\"id\":4}" +
                        "    ],\n" +
                        "    \"medicines\":[" +
                        "        {\"medicineId\":{\"id\":2}}," +
                        "        {\"medicineId\":{\"id\":3}}" +
                        "    ]" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.firstName", is("Alexandre")),
                        jsonPath("$.email", is("alexandrekerestes@exemplo.fr")),
                        jsonPath("$.allergies[0].id", is(4)),
                        jsonPath("$.medicines[0].quantity", is(1)),
                        jsonPath("$.medicines[0].medicineId.id", is(2))
                );
    }

    @Test
    public void createNewPersonMissintAttributeTest() throws Exception{

        mockMvc.perform(post("/person").content("{" +
                        "    \"firstName\":\"Alexandre\"," +
                        "    \"birthday\":\"1987-11-24\"," +
                        "    \"phone\":\"1234567890\"," +
                        "    \"email\":\"alexandrekerestes@exemplo.fr\"," +
                        "    \"address\":{\"id\":1}," +
                        "    \"allergies\":[" +
                        "        {\"id\":4}" +
                        "    ],\n" +
                        "    \"medicines\":[" +
                        "        {\"medicineId\":{\"id\":2}}," +
                        "        {\"medicineId\":{\"id\":3}}" +
                        "    ]" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is missing attribute, make sure you entered at least (firstName, lastName, phone, email, birthday and address{id})"))
                );
    }

    @Test
    public void createNewPersonInternalErrorTest() throws Exception{

        when(personService.savePerson(any())).thenReturn(null);

        mockMvc.perform(post("/person").content("{" +
                        "    \"firstName\":\"Alexandre\"," +
                        "    \"lastName\":\"Kerestes\"," +
                        "    \"birthday\":\"1987-11-24\"," +
                        "    \"phone\":\"1234567890\"," +
                        "    \"email\":\"alexandrekerestes@exemplo.fr\"," +
                        "    \"address\":{\"id\":1}," +
                        "    \"allergies\":[" +
                        "        {\"id\":4}" +
                        "    ],\n" +
                        "    \"medicines\":[" +
                        "        {\"medicineId\":{\"id\":2}}," +
                        "        {\"medicineId\":{\"id\":3}}" +
                        "    ]" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There was an error to save de Person"))
                );
    }

     @Test
     public void updatePersonTest() throws Exception{
        Person person = newPerson();
        person.setFirstName("Robert");
        when(personService.getPerson(anyLong())).thenReturn(Optional.of(newPerson()));
        when(personService.savePerson(any())).thenReturn(person);

        mockMvc.perform(put("/person").content("{\"id\":28, \"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.lastName", is("KERESTES")),
                        jsonPath("$.firstName", is("Robert"))
                );
     }

    @Test
    public void updatePersonNoIdTest() throws Exception{

        mockMvc.perform(put("/person").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("The Person's id must not be null"))
                );
    }

    @Test
    public void updatePersonNoPersonTest() throws Exception{
        when(personService.getPerson(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/person").content("{\"id\":28, \"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no person with this id"))
                );
    }

    @Test
    public void updatePersonIdTest() throws Exception{
        Person person = newPerson();
        person.setFirstName("Robert");
        when(personService.getPerson(anyLong())).thenReturn(Optional.of(newPerson()));
        when(personService.savePerson(any())).thenReturn(person);

        mockMvc.perform(put("/person/{id}", 28).content("{\"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.lastName", is("KERESTES")),
                        jsonPath("$.firstName", is("Robert"))
                );
    }

    @Test
    public void updatePersonIdNoIdTest() throws Exception{

        mockMvc.perform(put("/person/{id}", 0).content("{\"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("The Person's id must not be null"))
                );
    }

    @Test
    public void updatePersonIdNoPersonTest() throws Exception{
        when(personService.getPerson(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/person/{id}", 28).content("{\"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no person with this id"))
                );
    }

    @Test
    public void deletePersonTest() throws Exception {
        mockMvc.perform(delete("/person/{id}", 0))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid ID"))
                );
    }

    public Person newPerson(){
        Calendar calendar = Calendar.getInstance();
        Person person = new Person();
        Allergy allergy = new Allergy();
        Medicine medicine1 = new Medicine();
        Medicine medicine2 = new Medicine();

        medicine1.setId(2L);
        medicine1.setName("aznol");
        medicine2.setId(3L);
        medicine2.setName("aznol");

        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setQuantity(1);
        patientMedicine1.setMedicineId(medicine1);

        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setMedicineId(medicine2);
        patientMedicine2.setQuantity(1);

        allergy.setId(4L);
        allergy.setName("peanut");

        person.setId(28L);
        person.setFirstName("Alexandre");
        person.setLastName("KERESTES");
        calendar.set(1987, 11, 24);
        person.setBirthday(calendar.getTime());
        person.setPhone("1234567890");
        person.setEmail("alexandrekerestes@exemplo.fr");
        person.setMedicines(Arrays.asList(patientMedicine1, patientMedicine2));
        person.setAllergies(Arrays.asList(allergy));

        return person;
    }
}
