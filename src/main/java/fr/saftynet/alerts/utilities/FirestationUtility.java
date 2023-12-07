package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;

import java.util.*;
import java.util.stream.Collectors;

public class FirestationUtility {

    public static Set<String> getPhones(List<Address> addresses){
        return addresses.stream()
                .flatMap(address -> address.getPersons().stream())
                .map(person -> person.getPhone())
                .collect(Collectors.toSet());
    }

}
