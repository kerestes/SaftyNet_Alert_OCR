package fr.saftynet.alerts.integration;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FirestationServiceIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    FirestationService firestationService;

    @Autowired
    AddressService addressService;

    @Test
    public void getFirestationIT(){
        Optional<Firestation> firestation = firestationService.getFirestation(1L);

        assertTrue(firestation.isPresent());
        assertEquals("Firestation 1", firestation.get().getName());
    }

    @Test
    public void saveFirestationIT(){
        Firestation firestation = new Firestation();
        firestation.setName("New Firestation");

        firestation = firestationService.saveFirestation(firestation);

        assertNotNull(firestation);
        assertEquals("New Firestation", firestationService.getFirestation(firestation.getId()).get().getName());
    }

    @Test
    public void getPersonsPerFirestationIT(){
        List<Address> addresses = firestationService.getPersonsPerFirestation(1L);
        assertEquals(1, addresses.get(0).getPersons().size());
        assertEquals("CADIGAN", addresses.get(0).getPersons().get(0).getLastName());
        assertEquals("947 E. Rose Dr", addresses.get(1).getAddress());
    }

    @Test
    public void addFirestationToAddressIT(){
        Optional<Address> optionalAddress =  addressService.getAddress(1L);
        assertEquals("Firestation 3",  optionalAddress.get().getFirestation().getName());
        optionalAddress.get().getFirestation().setId(1L);
        Address address = firestationService.addFirestationToAddress(optionalAddress.get());
        assertEquals("Firestation 1", address.getFirestation().getName());
    }

    @Test
    public void deleteFirestationIT() throws Exception {
        Optional<Firestation> firestation = firestationService.getFirestation(2L);

        assertTrue(firestation.isPresent());

        mockMvc.perform(delete("/firestation/{id}", 2))
                .andExpect(
                        status().isOk()
                );

        firestation = firestationService.getFirestation(2L);

        assertTrue(firestation.isEmpty());
    }

    @Test
    public void deleteMappingFirestationIT() throws Exception {
        Optional<Address> address = addressService.getAddress(3L);
        assertTrue(address.isPresent());
        assertNotNull(address.get().getFirestation());

        mockMvc.perform(delete("/firestation/toaddress/{id}", 3L))
                .andExpect(
                        status().isOk()
                );

        address = addressService.getAddress(3L);
        assertTrue(address.isPresent());
        assertNull(address.get().getFirestation());
    }
}
