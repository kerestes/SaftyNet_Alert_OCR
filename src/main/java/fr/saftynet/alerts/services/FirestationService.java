package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.repositories.AddressRepository;
import fr.saftynet.alerts.repositories.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirestationService {

    @Autowired
    private FirestationRepository firestationRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Optional<Firestation> getFirestation(Long id){return firestationRepository.findById(id);}

    public Firestation saveFirestation(Firestation firestation){ return firestationRepository.save(firestation);}

    public Address addFirestationToAddress(Address address){return addressRepository.save(address);}


    public Address deleteMappingFirestation(Long id){
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()) {
            Address realAddress = address.get();
            realAddress.setFirestation(null);
            return addressRepository.save(realAddress);
        } else {
            return null;
        }
    }

    public void deleteFirestation(Long id){firestationRepository.deleteById(id);}
}
