package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.FirestationUtility;
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
    public HashMap<String, Set<String>> getPhoneNumberPerFirestation(@Param("firestationId") Long firestationId){
        Optional<Firestation> realFirestation = firestationService.getFirestation(firestationId);
        if(realFirestation.isPresent()){
            HashMap<String, Set<String>> returnHashMap = new HashMap<>();
            returnHashMap.put(realFirestation.get().getName(),  FirestationUtility.getPhones(firestationService.getPersonsPerFirestation(firestationId)));
            return returnHashMap;
        }
        return null;
    }

    @GetMapping("/firestation")
    public List<Address> getListPerson(@Param("stationNumber") Long stationNumber){
        if(stationNumber != null){
            List<Address> addresses = firestationService.getPersonsPerFirestation(stationNumber);
            return AddressUtility.setMinorAndMajorList(addresses);
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
        if(id > 0 && firestationService.getFirestation(id).isPresent()){
            firestation.setId(id);
            return firestationService.saveFirestation(firestation);
        }
        return null;
    }

    @PutMapping("/firestation/toaddress/{id}")
    public Address addFirestationToAddress(@RequestBody ObjectNode objectNode, @PathVariable Long id){
        Long firestationId = objectNode.get("firestationId").asLong();
        Optional<Firestation> firestation = firestationService.getFirestation(firestationId);
        if(firestation.isPresent()) {
            Optional<Address> realAddress = addressService.getAddress(id);
            if(realAddress.isPresent()){
                realAddress.get().setFirestation(firestation.get());
                Address address = firestationService.addFirestationToAddress(realAddress.get());
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
    public void deleteMappingFirestation(@PathVariable Long id){
        firestationService.deleteMappingFirestation(id);
    }

}
