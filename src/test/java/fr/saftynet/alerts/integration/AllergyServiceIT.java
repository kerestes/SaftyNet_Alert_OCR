package fr.saftynet.alerts.integration;

import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AllergyService;
import fr.saftynet.alerts.services.PersonService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AllergyServiceIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AllergyService allergyService;
    @Autowired
    PersonService personService;

    @Test
    public void saveAllergyIT(){
        Allergy allergy = new Allergy();
        allergy.setName("Test allergy");
        Allergy savedAllergy = allergyService.saveAllergy(allergy);

        assertEquals("Test allergy", savedAllergy.getName());
    }

    @Test
    public void getAllergyIT(){
        Optional<Allergy> optionalAllergy = allergyService.getAllergy(1L);
        assertTrue(optionalAllergy.isPresent());
        assertEquals("aznol", optionalAllergy.get().getName());
    }

    @Test
    public void getAllergyNullIT(){
        Optional<Allergy> optionalAllergy = allergyService.getAllergy(1000L);
        assertTrue(optionalAllergy.isEmpty());
    }

    @Test
    public void deleteAllergyFromPersonIT() throws Exception {
        mockMvc.perform(put("/allergy/{personId}", 1).content("{\"allergyId\":1}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.allergies[-1].name", is("aznol"))
                );

        Optional<Person> optinalPerson = personService.getPerson(1L);

        assertTrue(optinalPerson.isPresent());
        assertEquals("aznol", optinalPerson.get().getAllergies().get(optinalPerson.get().getAllergies().size() -1).getName());

        mockMvc.perform(delete("/allergy/{personId}/{allergyId}", 1, 1)).andExpect(status().isOk());

        optinalPerson = personService.getPerson(1L);

        assertTrue(optinalPerson.isPresent());
        assertNotEquals("aznol", optinalPerson.get().getAllergies().get(optinalPerson.get().getAllergies().size() -1).getName());
    }
}
