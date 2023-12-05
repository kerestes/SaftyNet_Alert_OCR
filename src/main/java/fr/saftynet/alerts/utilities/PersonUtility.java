package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Person;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PersonUtility {

    public static int getAge(Date birthday){
        LocalDate today = LocalDate.now();
        LocalDate birthdayDate =  birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return birthdayDate.until(today).getYears();
    }

    public static Person createUpdate(Person personUpdate, Person personOld) {
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
}
