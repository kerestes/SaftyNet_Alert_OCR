package fr.saftynet.alerts.unitaire;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.utilities.AddressUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

public class AddressUtilityTest {

    Address address = new Address();
    Firestation firestation = new Firestation();;
    Person person1 = new Person();
    Person person2 = new Person();

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

        Assertions.assertTrue(resultAgeNotNull);
        Assertions.assertTrue(resultBirthdayNull);
    }

    @Test
    public void removeLastNameTest(){
        AddressUtility.removeLastName(Arrays.asList(address), "kerestes");

        Assertions.assertTrue(!address.getPersons().contains(person2));
        Assertions.assertTrue(address.getPersons().contains(person1));
    }


    @Test
    public void setMinorAndMajorListTest(){
        AddressUtility.setMinorAndMajorList(Arrays.asList(address));

        Assertions.assertTrue(address.getPersons() == null);
        Assertions.assertTrue(address.getMinor().size() == 1);
        Assertions.assertTrue(address.getMajor().size() == 1);
    }
}
