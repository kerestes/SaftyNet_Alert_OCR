package fr.saftynet.alerts.integration;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.PersonService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonServiceIT {
    @Autowired
    PersonService personService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getPersonIT(){
        Optional<Person> optionalPerson = personService.getPerson(1L);
        assertTrue(optionalPerson.isPresent());
        assertEquals("John", optionalPerson.get().getFirstName());
        assertEquals("1509 Culver St", optionalPerson.get().getAddress().getAddress());
    }

    @Test
    public void cGetEmailPerCityIT(){
        List<String> emails = personService.getEmailPerCity("Culver");
        assertTrue(!emails.isEmpty());
        assertEquals(16, emails.size());
        assertTrue(emails.contains("lily@email.com"));
    }

    @Test
    public void aSavePersonIT(){
        Calendar calendar = Calendar.getInstance();

        Address address = new Address();
        address.setId(1L);

        Person person = new Person();
        person.setFirstName("Alexandre");
        person.setLastName("KERESTES");
        calendar.set(1987, 11, 24);
        person.setBirthday(calendar.getTime());
        person.setPhone("0123456789");
        person.setEmail("alexandre@email.com");
        person.setAddress(address);

        Person personResult = personService.savePerson(person);

        assertEquals(24L, personResult.getId());
        assertEquals("Alexandre", personResult.getFirstName());
        assertEquals("alexandre@email.com", personResult.getEmail());
    }

    @Test
    public void bDeletePersonIT() throws Exception{
        Optional<Person> optionalPerson = personService.getPerson(3L);
        assertTrue(optionalPerson.isPresent());

        mockMvc.perform(delete("/person/{id}", 3))
                .andExpect( status().isOk());

        Optional<Person> optionalPersonResult = personService.getPerson(3L);
        assertTrue(optionalPersonResult.isEmpty());
    }
}
