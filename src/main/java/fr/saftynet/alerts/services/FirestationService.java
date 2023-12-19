package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.repositories.IAddressRepository;
import fr.saftynet.alerts.repositories.IFirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirestationService {

    @Autowired
    private IFirestationRepository firestationRepository;
    @Autowired
    private IAddressRepository addressRepository;

    public Optional<Firestation> getFirestation(final Long id){return firestationRepository.getFirestation(id);}

    public Firestation saveFirestation(final Firestation firestation){ return firestationRepository.saveFirestation(firestation);}

    public Address addFirestationToAddress(final Address address){return addressRepository.updateAddress(address);}

    public void deleteFirestation(final Long id){firestationRepository.deleteFirestation(id);}

    public List<Address> getPersonsPerFirestation(final Long id){return firestationRepository.getPersonsPerFirestation(id);}

    public void deleteMappingFirestation(final Long id){
        Optional<Address> address = addressRepository.getAddress(id);
        if(address.isPresent()) {
            address.get().setFirestation(null);
            addressRepository.updateAddress(address.get());
        }
    }
}
