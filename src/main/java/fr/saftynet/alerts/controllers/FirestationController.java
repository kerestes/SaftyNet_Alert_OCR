package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;
    @Autowired
    private AddressService addressService;

    @PostMapping("/firestation")
    public Firestation createFirestation(@RequestBody Firestation firestation){return firestationService.saveFirestation(firestation);}

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
        Long fistationId = objectNode.get("fistationId").asLong();
        Optional<Firestation> firestation = firestationService.getFirestation(fistationId);
        if(firestation.isPresent()) {
            Optional<Address> realAddress = addressService.getAddress(id);
            if(realAddress.isPresent()){
                Address address = realAddress.get();
                address.setFirestation(firestation.get());
                return firestationService.addFirestationToAddress(address);
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
