package fr.saftynet.alerts.unitaire.SpringContext;

import fr.saftynet.alerts.controllers.FirestationController;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import nl.altindag.log.LogCaptor;
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

import static org.junit.jupiter.api.Assertions.*;
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
    public void createFirestationNoNameTest() throws Exception{

        mockMvc.perform(post("/firestation").content("{}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void createFirestationInternalErrorTest() throws Exception{
        when(firestationService.saveFirestation(any())).thenReturn(null);

        mockMvc.perform(post("/firestation").content("{\"name\": \"New Firestation\"}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
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

        mockMvc.perform(put("/firestation").content("{}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void updateFirestationNoNameTest() throws Exception {

        mockMvc.perform(put("/firestation").content("{\"id\":5}").contentType((MediaType.APPLICATION_JSON)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void updateFirestationNoFirestationTest() throws Exception {
        when(firestationService.getFirestation(any())).thenReturn(Optional.empty());

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

        mockMvc.perform(put("/firestation/{id}", 0).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void updateFirestationIdNoNameTest() throws Exception{

        mockMvc.perform(put("/firestation/{id}", 3).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void updateFirestationIdNoFirestationTest() throws Exception{
        when(firestationService.getFirestation(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/firestation/{id}", 3).content("{\"name\": \"Changement de nom\"}").contentType(MediaType.APPLICATION_JSON))
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

    @Test
    public void addFirestationToAddressNoFirestationIdTest() throws Exception{

        mockMvc.perform(put("/firestation/toaddress/{id}", 2).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()

                );
    }

    @Test
    public void addFirestationToAddressNoFirestationTest() throws Exception{

        when(firestationService.getFirestation(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/firestation/toaddress/{id}", 2).content("{\"firestationId\" : 4}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()

                );
    }

    @Test
    public void addFirestationToAddressNoAddressTest() throws Exception{
        Optional<Firestation> firestation = Optional.of(new Firestation());
        firestation.get().setId(4L);
        firestation.get().setName("Firestation 4");

        when(firestationService.getFirestation(any())).thenReturn(firestation);
        when(addressService.getAddress(2L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/firestation/toaddress/{id}", 2).content("{\"firestationId\" : 4}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()

                );
    }

    @Test
    public void deleteFirestationTest() throws Exception {
        mockMvc.perform(delete("/firestation/{id}", 0))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void deleteMappingFirestationTest() throws Exception {
        mockMvc.perform(delete("/firestation/toaddress/{id}", 0))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

}
