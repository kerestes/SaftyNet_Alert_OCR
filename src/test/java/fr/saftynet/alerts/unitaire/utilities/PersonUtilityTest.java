package fr.saftynet.alerts.unitaire.utilities;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.utilities.PersonUtility;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonUtilityTest {

    LogCaptor logCaptor = LogCaptor.forClass(PersonUtility.class);
    @Test
    public void getAgeTest(){
        LocalDate localDate = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(localDate.getYear() - 17, localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        Date date = calendar.getTime();

        Assertions.assertEquals(17, PersonUtility.getAge(date));
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Converting birthday(Date) into age(int)"));
    }

    @Test
    public void createUpdateTest(){
        Person personUpdate = new Person();

        Person personOld = new Person();
        personOld.setFirstName("Alexandre");
        personOld.setLastName("KERESTES");

        Assertions.assertEquals("KERESTES", personOld.getLastName());
        Assertions.assertNull(personUpdate.getFirstName());

        personUpdate.setLastName("dupont");
        PersonUtility.createUpdate(personUpdate, personOld);

        Assertions.assertEquals("DUPONT", personUpdate.getLastName());
        Assertions.assertEquals("Alexandre", personUpdate.getFirstName());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Merging saved person"));
    }

    @Test
    public void createUpdateLastNameTest(){
        Person personUpdate = new Person();

        Person personOld = new Person();
        personOld.setFirstName("Alexandre");
        personOld.setLastName("KERESTES");

        PersonUtility.createUpdate(personUpdate, personOld);

        Assertions.assertEquals("KERESTES", personUpdate.getLastName());
        Assertions.assertEquals("Alexandre", personUpdate.getFirstName());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Merging saved person"));
    }

    @Test
    public void changeBirthdayForAgeTest(){
        LocalDate localDate = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(localDate.getYear() - 17, localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        Date date = calendar.getTime();

        Person person = new Person();
        person.setBirthday(date);

        Assertions.assertEquals(date, person.getBirthday());
        Assertions.assertNull(person.getAge());

        PersonUtility.changeBirthdayForAge(person);

        Assertions.assertEquals(17, person.getAge());
        Assertions.assertNull(person.getBirthday());
        assertTrue(logCaptor.getDebugLogs().get(0).contains("Setting null birthday(Date) and converting into age(int)"));
    }
}
