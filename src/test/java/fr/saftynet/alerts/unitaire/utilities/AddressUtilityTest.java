package fr.saftynet.alerts.unitaire.utilities;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.utilities.AddressUtility;
import nl.altindag.log.LogCaptor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

public class AddressUtilityTest {

    Address address = new Address();
    Firestation firestation = new Firestation();;
    Person person1 = new Person();
    Person person2 = new Person();
    LogCaptor logCaptor = LogCaptor.forClass(AddressUtility.class);
    @BeforeEach
    public void init(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(1987, 11, 24);
        Date date1 = calendar.getTime();

        person1.setId(1L);
        person1.setLastName("KERESTES");
        person1.setBirthday(date1);

        LocalDate localDate = LocalDate.now();
        calendar.set(localDate.getYear() - 17, localDate.getMonthValue(), localDate.getDayOfMonth());
        Date date2 = calendar.getTime();

        person1.setId(2L);
        person2.setLastName("RRODRIGUES");
        person2.setBirthday(date2);

        firestation.setId(1L);
        firestation.setName("Firestation Test");

        address.setId(1L);
        address.setFirestation(firestation);
        address.setAddress("123 street Test");
        address.setCity("Test City");
        address.setZip("12345");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);
        address.setPersons(personList);
    }

    @Test
    public void changeBirthdayForAgeTest(){

        AddressUtility.changeBirthdayForAge(address);
        boolean resultAgeNotNull = address.getPersons().stream().allMatch(person -> person.getAge() != null);
        boolean resultBirthdayNull = address.getPersons().stream().allMatch(person -> person.getBirthday() == null);

        assertTrue(resultAgeNotNull);
        assertTrue(resultBirthdayNull);
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Converting date to age for address"));

    }

    @Test
    public void removeLastNameTest(){
        AddressUtility.removeLastName(Arrays.asList(address), "kerestes");

        assertTrue(!address.getPersons().contains(person2));
        assertTrue(address.getPersons().contains(person1));

        assertTrue(logCaptor.getDebugLogs().get(0).contains("Removing people who do not have the lastname"));
    }


    @Test
    public void setMinorAndMajorListTest(){
        AddressUtility.setMinorAndMajorList(Arrays.asList(address));

        assertTrue(address.getPersons() == null);
        assertTrue(address.getMinor().size() == 1);
        assertTrue(address.getMajor().size() == 1);

        assertTrue(logCaptor.getDebugLogs().get(0).contains("Defining majors and minors based on the address"));
    }
}
