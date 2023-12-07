package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Person;

import java.util.ArrayList;
import java.util.List;

public class AddressUtility {
    public static Address changeBirthdayForAge(Address address){
        address.getPersons().stream().forEach(person -> {
            PersonUtility.changeBirthdayForAge(person);
        });
        return address;
    }

    public static List<Address> removeLastName(List<Address> addresses, String lastName) {
        final String finalLastName = lastName.toUpperCase();
        for(Address address: addresses){
            address.getPersons().removeIf(person -> !person.getLastName().contains(finalLastName));
            for(Person person: address.getPersons()){
                PersonUtility.changeBirthdayForAge(person);
            }
        }
        return addresses;
    }

    public static List<Address> setMinorAndMajorList(List<Address> addresses){
        for(Address address: addresses){
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
