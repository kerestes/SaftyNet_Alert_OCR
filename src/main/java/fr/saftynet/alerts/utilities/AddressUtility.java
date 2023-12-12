package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AddressUtility {

    private static final Logger logger = LogManager.getLogger(AddressUtility.class);

    public static Address changeBirthdayForAge(Address address){
        logger.debug("Converting date to age for address" + address.getAddress());
        address.getPersons().stream().forEach(person -> {
            PersonUtility.changeBirthdayForAge(person);
        });
        return address;
    }

    public static List<Address> removeLastName(List<Address> addresses, String lastName) {
        final String finalLastName = lastName.toUpperCase();
        for(Address address: addresses){
            logger.debug("Removing people who do not have the lastname " + lastName + " from the address " + address.getAddress());
            address.getPersons().removeIf(person -> !person.getLastName().contains(finalLastName));
            for(Person person: address.getPersons()){
                PersonUtility.changeBirthdayForAge(person);
            }
        }
        return addresses;
    }

    public static List<Address> setMinorAndMajorList(List<Address> addresses){
        for(Address address: addresses){
            logger.debug("Defining majors and minors based on the address " + address.getAddress());
            address.setMinor(new ArrayList<>());
            address.setMajor(new ArrayList<>());
            for(Person person: address.getPersons()){
                PersonUtility.changeBirthdayForAge(person);
                MedicalRecordUtility.setNullMecialRecord(person);
                if (person.getAge() <= 18)
                    address.getMinor().add(person);
                else
                    address.getMajor().add(person);
            }
            address.setPersons(null);
        }
        return addresses;
    }
}
