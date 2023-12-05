package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import fr.saftynet.alerts.utilities.PersonUtility;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;
    @Autowired
    private AddressService addressService;

    Logger logger = (Logger) LoggerFactory.getLogger(FirestationController.class);


    @GetMapping("/phoneAlert")
    public HashMap<String, List<String>> getPhoneNumberPerFirestation(@Param("firestation") Long firestation){
        Optional<Firestation> realFirestation = firestationService.getFirestation(firestation);
        if(realFirestation.isPresent()){
            List<Address> addresses = firestationService.getPersonsPerFirestation(firestation);
            List<String> phones = new ArrayList<>();
            for(Address address: addresses){
                for(Person person: address.getPersons()){
                    if(!phones.contains(person.getPhone()))
                        phones.add(person.getPhone());
                }
            }
            HashMap<String, List<String>> returnHashMap = new HashMap<>();
            returnHashMap.put(realFirestation.get().getName(), phones);
            return returnHashMap;
        }
        return null;
    }

    @GetMapping("/firestation")
    public List<Address> getListPerson(@Param("stationNumber") Long stationNumber){
        if(stationNumber != null){
            List<Address> addresses = firestationService.getPersonsPerFirestation(stationNumber);
            for(Address address: addresses){
                address.setMinor(new ArrayList<>());
                address.setMajor(new ArrayList<>());
                for(Person person: address.getPersons()){
                    int age = PersonUtility.getAge(person.getBirthday());
                    person.setBirthday(null);
                    person.setEmail(null);
                    person.setAllergies(null);
                    person.setMedicines(null);
                    person.getAddress().setFirestation(null);
                    if (age > 18)
                        address.getMinor().add(person);
                    else
                        address.getMajor().add(person);
                }
                address.setPersons(null);
            }
            return addresses;
        }
        return null;
    }

    @PostMapping("/firestation")
    public Firestation createFirestation(@RequestBody Firestation firestation){
        return firestationService.saveFirestation(firestation);
    }

    @PutMapping("/firestation")
    public Firestation updateFirestation(@RequestBody Firestation firestation){
        if(firestation.getId() != 0 && firestationService.getFirestation(firestation.getId()).isPresent())
            return firestationService.saveFirestation(firestation);
        return null;
    }

    @PutMapping("/firestation/{id}")
    public Firestation updateFirestationId(@RequestBody Firestation firestation, @PathVariable Long id){
        if(id > 0 && firestationService.getFirestation(firestation.getId()).isPresent()){
            firestation.setId(id);
            return firestationService.saveFirestation(firestation);
        }
        return null;
    }

    @PutMapping("/firestation/toaddress/{id}")
    public Address addFirestationToAddress(@RequestBody ObjectNode objectNode, @PathVariable Long id){
        Long fistationId = objectNode.get("firestationId").asLong();
        Optional<Firestation> firestation = firestationService.getFirestation(fistationId);
        if(firestation.isPresent()) {
            Optional<Address> realAddress = addressService.getAddress(id);
            if(realAddress.isPresent()){
                Address address = realAddress.get();
                address.setFirestation(firestation.get());
                address = firestationService.addFirestationToAddress(address);
                address.setPersons(null);
                return address;
            }
        }
        return null;
    }

    @DeleteMapping("/firestation/{id}")
    public void deleteFirestation(@PathVariable Long id){
        firestationService.deleteFirestation(id);
    }

    @DeleteMapping("/firestation/toaddress/{id}")
    public Address deleteMappingFirestation(@PathVariable Long id){
        return firestationService.deleteMappingFirestation(id);
    }

}
