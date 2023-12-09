package fr.saftynet.alerts.integration;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.services.AddressService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class AddressServiceIT {

    @Autowired
    AddressService addressService;

    @Test
    public void getCityIT(){
        Optional<String> city = addressService.getCity("Culver");
        assertTrue(city.isPresent());
        assertEquals("Culver", city.get());
    }

    @Test
    public void getCityNullIT(){
        Optional<String> city = addressService.getCity("ABC");
        assertTrue(city.isEmpty());
    }

    @Test
    public void getAddressIT(){
        Optional<Address> optionalAddress = addressService.getAddress(1L);
        assertTrue(optionalAddress.isPresent());
        assertEquals("1509 Culver St", optionalAddress.get().getAddress());
    }

    @Test
    public void getAddressNullIT(){
        Optional<Address> optionalAddress = addressService.getAddress(1000L);
        assertTrue(optionalAddress.isEmpty());
    }

    @Test
    public void getPersonByLastNameIT(){
        List<Address> addresses = addressService.getPersonByLastName("boyd");
        assertTrue(!addresses.isEmpty());
        assertEquals("1509 Culver St", addresses.get(0).getAddress());
        assertEquals("112 Steppes Pl", addresses.get(1).getAddress());
        assertEquals(5, addresses.get(0).getPersons().size());
    }

    @Test
    public void getPersonByLastNameNullIT(){
        List<Address> addresses = addressService.getPersonByLastName("123");
        assertTrue(addresses.isEmpty());
    }

    @Test
    public void getAddressByNameIT(){
        Optional<Address> addresses = addressService.getAddressByName("Downing");
        assertTrue(addresses.isPresent());
        assertEquals("892 Downing Ct", addresses.get().getAddress());
        assertEquals("Sophia", addresses.get().getPersons().get(0).getFirstName());
    }

    @Test
    public void getAddressByNameNullIT() {
        Optional<Address> addresses = addressService.getAddressByName("XXX");
        assertTrue(addresses.isEmpty());
    }
}
