package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PersonUtility {

    private static final Logger logger = LogManager.getLogger();

    public static int getAge(Date birthday){
        logger.debug("Converting birthday(Date) into age(int)");
        LocalDate today = LocalDate.now();
        LocalDate birthdayDate =  birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return birthdayDate.until(today).getYears();
    }

    public static Person createUpdate(Person personUpdate, Person personOld) {
        logger.debug("Merging saved person (" + personOld.getFirstName() + " " + personOld.getLastName() + ") with updated information");
        if (personUpdate.getFirstName() == null || personUpdate.getFirstName().isEmpty())
            personUpdate.setFirstName(personOld.getFirstName());

        if (personUpdate.getLastName() == null || personUpdate.getLastName().isEmpty())
            personUpdate.setLastName(personOld.getLastName());
        else
            personUpdate.setLastName(personUpdate.getLastName().toUpperCase());

        if (personUpdate.getAddress() == null)
            personUpdate.setAddress(personOld.getAddress());
        if (personUpdate.getBirthday() == null)
            personUpdate.setBirthday(personOld.getBirthday());
        if (personUpdate.getPhone() == null || personUpdate.getPhone().isEmpty())
            personUpdate.setPhone(personOld.getPhone());
        if (personUpdate.getEmail() == null || personUpdate.getEmail().isEmpty())
            personUpdate.setEmail(personOld.getEmail());

        personUpdate.setId(personOld.getId());
        personUpdate.setMedicines(personOld.getMedicines());
        personUpdate.setAllergies(personOld.getAllergies());
        return personUpdate;
    }

    public static Person changeBirthdayForAge(Person person){
        logger.debug("Setting null birthday(Date) and converting into age(int)");
        person.setAge(getAge(person.getBirthday()));
        person.setBirthday(null);
        return person;
    }
}
