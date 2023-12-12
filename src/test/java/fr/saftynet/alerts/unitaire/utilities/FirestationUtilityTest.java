package fr.saftynet.alerts.unitaire.utilities;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.utilities.FirestationUtility;
import nl.altindag.log.LogCaptor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

public class FirestationUtilityTest {
    Person person1;
    Person person2;
    Person person3;
    Person person4;
    Address address;
    LogCaptor logCaptor = LogCaptor.forClass(FirestationUtility.class);

    @BeforeEach
    public void init(){
        person1 = new Person();
        person2 = new Person();
        person3 = new Person();
        person4 = new Person();
        address = new Address();
    }
    @Test
    public void getPhonesTest(){

        person1.setPhone("0123456789");
        person2.setPhone("1234567890");
        person3.setPhone("2345678901");
        person4.setPhone("4567890123");

        address.setPersons(Arrays.asList(person1, person2, person3, person4));
        Set<String> phones = FirestationUtility.getPhones(Arrays.asList(address));
        assertEquals(4, phones.size());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Getting phone numbers from address"));

    }

    @Test
    public void repeatedPhonesTest(){
        person1.setPhone("0123456789");
        person2.setPhone("0123456789");
        person3.setPhone("0123456789");
        person4.setPhone("0123456789");

        address.setPersons(Arrays.asList(person1, person2, person3, person4));
        Set<String> phones = FirestationUtility.getPhones(Arrays.asList(address));
        assertEquals(1, phones.size());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Getting phone numbers from address"));
    }
}
