package fr.saftynet.alerts.unitaire.SpringContext;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
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
public class FirestationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FirestationService firestationService;

    @MockBean
    AddressService addressService;

    @Test
    public void getPhoneAlertTest() throws Exception {
        Optional<Firestation> optionalFirestation = Optional.of(new Firestation());
        optionalFirestation.get().setId(1L);
        optionalFirestation.get().setName("Firestation 1");

        when(firestationService.getFirestation(any())).thenReturn(optionalFirestation);
        when(firestationService.getPersonsPerFirestation(1L)).thenReturn(makeAddressList());

        mockMvc.perform(get("/phoneAlert?firestationId={id}", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("['Firestation 1']", CoreMatchers.hasItem("8418747462")),
                        jsonPath("['Firestation 1']", hasSize(5))
                );
    }

    @Test
    public void getFirestationTest() throws Exception {
        when(firestationService.getPersonsPerFirestation(1L)).thenReturn(makeAddressList());

        mockMvc.perform(get("/firestation?stationNumber={id}", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].address", CoreMatchers.is("644 Gershwin Cir")),
                        jsonPath("$[0].major.[0].lastName", CoreMatchers.is("DUCAN")),
                        jsonPath("$[0].minor", hasSize(0)),
                        jsonPath("$[1].id", CoreMatchers.is(9))
                        );
    }

    @Test
    public void createFirestationTest() throws Exception{
        Firestation firestation = new Firestation();
        firestation.setId(5L);
        firestation.setName("New Firestation");
        when(firestationService.saveFirestation(any())).thenReturn(firestation);

        mockMvc.perform(post("/firestation").content("{\"name\": \"New Firestation\"}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("New Firestation")),
                        jsonPath("$.id", is(5))
                );
    }

    @Test
    public void updateFirestationTest() throws Exception {
        Firestation firestation = new Firestation();
        firestation.setId(5L);
        firestation.setName("Changement de nom");
        Optional<Firestation> optionalFirestation = Optional.of(firestation);

        when(firestationService.saveFirestation(any())).thenReturn(firestation);
        when(firestationService.getFirestation(any())).thenReturn(optionalFirestation);

        mockMvc.perform(put("/firestation").content("{\"id\":5, \"name\": \"Changement de nom\"}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("Changement de nom")),
                        jsonPath("$.id", is(5))
                );
    }

    @Test
    public void updateFirestationNoIdTest() throws Exception {
        Optional<Firestation> optionalFirestation = Optional.empty();
        when(firestationService.getFirestation(any())).thenReturn(optionalFirestation);

        mockMvc.perform(put("/firestation").content("{\"id\":5, \"name\": \"Changement de nom\"}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void updateFirestationIdTest() throws Exception{
        Firestation firestation = new Firestation();
        firestation.setId(2L);
        firestation.setName("Changement de nom");
        Optional<Firestation> optionalFirestation = Optional.of(firestation);

        when(firestationService.saveFirestation(any())).thenReturn(firestation);
        when(firestationService.getFirestation(any())).thenReturn(optionalFirestation);

        mockMvc.perform(put("/firestation/{id}", 2).content("{\"id\":3, \"name\": \"Changement de nom\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("Changement de nom")),
                        jsonPath("$.id", is(2))
                );
    }

    @Test
    public void updateFirestationIdNoIdTest() throws Exception{
        Firestation firestation = new Firestation();
        firestation.setId(2L);
        firestation.setName("Changement de nom");
        Optional<Firestation> optionalFirestation = Optional.of(firestation);

        when(firestationService.saveFirestation(any())).thenReturn(firestation);
        when(firestationService.getFirestation(any())).thenReturn(optionalFirestation);

        mockMvc.perform(put("/firestation/{id}", 0).content("{\"id\":3, \"name\": \"Changement de nom\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void addFirestationToAddressTest() throws Exception{
        Optional<Firestation> firestation = Optional.of(new Firestation());
        firestation.get().setId(4L);
        firestation.get().setName("Firestation 4");

        Optional<Address> address = Optional.of(new Address());
        address.get().setId(2L);
        address.get().setAddress("29 15th St");
        address.get().setCity("Culver");
        address.get().setZip("97451");
        address.get().setFirestation(firestation.get());
        address.get().setPersons(new ArrayList<>());

        when(firestationService.getFirestation(any())).thenReturn(firestation);
        when(addressService.getAddress(2L)).thenReturn(address);
        when(firestationService.addFirestationToAddress(any())).thenReturn(address.get());

        mockMvc.perform(put("/firestation/toaddress/{id}", 2).content("{\"firestationId\" : 4}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.firestation.id", is(4)),
                        jsonPath("$.firestation.name", is("Firestation 4")),
                        jsonPath("$.id", is(2)),
                        jsonPath("$.address", is("29 15th St"))

                );
    }

    public List<Address> makeAddressList(){
        List<Address> addresses = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        Address address = new Address();
        address.setId(4L);
        address.setAddress("644 Gershwin Cir");
        address.setCity("Culver");
        address.setZip("97451");

        Address address1 = new Address();
        address1.setId(9L);
        address1.setAddress("908 73rd St");
        address1.setCity("Culver");
        address1.setZip("97451");

        Address address2 = new Address();
        address2.setId(10L);
        address2.setAddress("947 E. Rose Dr");
        address2.setCity("Culver");
        address2.setZip("97451");

        Address address3 = new Address();
        address2.setId(11L);
        address2.setAddress("951 LoneTree Rd");
        address2.setCity("Culver");
        address2.setZip("97451");

        Person person1 = new Person();
        person1.setFirstName("Brain");
        person1.setLastName("STELZER");
        calendar.set(1975, 12, 06);
        person1.setBirthday(calendar.getTime());
        person1.setPhone("8418747784");

        Person person2 = new Person();
        person2.setFirstName("Shawna");
        person2.setLastName("STELZER");
        calendar.set(1980, 07, 8);
        person2.setBirthday(calendar.getTime());
        person2.setPhone("8418747784");

        Person person3 = new Person();
        person3.setFirstName("Kendrik");
        person3.setLastName("STELZER");
        calendar.set(2014, 3, 6);
        person3.setBirthday(calendar.getTime());
        person3.setPhone("8418747784");

        Person person4 = new Person();
        person4.setFirstName("Jamie");
        person4.setLastName("PETERS");
        calendar.set(1982, 3, 6);
        person4.setBirthday(calendar.getTime());
        person4.setPhone("8418747462");

        Person person5 = new Person();
        person5.setFirstName("Eric");
        person5.setLastName("CADIGAN");
        calendar.set(1975, 8, 6);
        person5.setBirthday(calendar.getTime());
        person5.setPhone("8418747458");

        Person person6 = new Person();
        person6.setFirstName("Peter");
        person6.setLastName("DUCAN");
        calendar.set(2000, 9, 6);
        person6.setBirthday(calendar.getTime());
        person6.setPhone("8418746512");

        Person person7 = new Person();
        person7.setFirstName("Reginold");
        person7.setLastName("WALKER");
        calendar.set(1979, 8, 30);
        person7.setBirthday(calendar.getTime());
        person7.setPhone("8418748547");

        address3.setPersons(Arrays.asList(person5));
        address2.setPersons(Arrays.asList(person1, person2, person3));
        address1.setPersons(Arrays.asList(person4, person7));
        address.setPersons(Arrays.asList(person6));

        addresses.add(address);
        addresses.add(address1);
        addresses.add(address2);
        addresses.add(address3);

        return addresses;
    }
}
