package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.repositories.AddressRepository;
import fr.saftynet.alerts.repositories.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirestationService {

    @Autowired
    private FirestationRepository firestationRepository;
    @Autowired
    private AddressRepository addressRepository;

    public Optional<Firestation> getFirestation(final Long id){return firestationRepository.findById(id);}

    public Firestation saveFirestation(final Firestation firestation){ return firestationRepository.save(firestation);}

    public Address addFirestationToAddress(final Address address){return addressRepository.save(address);}

    public void deleteFirestation(final Long id){firestationRepository.deleteById(id);}

    public List<Address> getPersonsPerFirestation(final Long id){return firestationRepository.personsPerFirestation(id);}

    public void deleteMappingFirestation(final Long id){
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()) {
            address.get().setFirestation(null);
            addressRepository.save(address.get());
        }
    }
}
