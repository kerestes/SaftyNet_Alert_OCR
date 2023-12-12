package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class FirestationUtility {

    private static final Logger logger = LogManager.getLogger(FirestationUtility.class);

    public static Set<String> getPhones(List<Address> addresses){
        return addresses.stream()
                .flatMap(address -> {
                    logger.debug("Getting phone numbers from address" + address.getAddress());
                    return address.getPersons().stream();
                })
                .map(person -> person.getPhone())
                .collect(Collectors.toSet());
    }

}
