package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;
    @Autowired
    private AddressService addressService;
    @PostMapping("/firestation")
    public Firestation createFirestation(@RequestBody Firestation firestation){return firestationService.createFirestation(firestation);}

    @PutMapping("/firestation/{id}")
    public Address addFirestationToAddress(@RequestBody ObjectNode objectNode, @PathVariable Long id){
        Long addressId = objectNode.get("addressId").asLong();
        Optional<Firestation> firestation = firestationService.getFirestation(id);
        if(firestation.isPresent()) {
            Optional<Address> realAddress = addressService.getAddress(addressId);
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

    @DeleteMapping("/firestation/mapping/{id}")
    public Address deleteMappingFirestation(@PathVariable Long id){
        return firestationService.deleteMappingFirestation(id);
    }

}
