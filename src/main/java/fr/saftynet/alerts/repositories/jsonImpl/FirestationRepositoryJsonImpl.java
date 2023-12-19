package fr.saftynet.alerts.repositories.jsonImpl;

import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.repositories.IFirestationRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FirestationRepositoryJsonImpl implements IFirestationRepository {
    @Override
    public Optional<Firestation> getFirestation(Long id) {
        return Optional.ofNullable(JsonReader.getFirestations().get(id));
    }

    @Override
    public List<Address> getPersonsPerFirestation(Long firestationId) {
        Collection<Address> addresses = JsonReader.getAddresses().values();
        return addresses.stream().filter(address -> address.getFirestation() != null && address.getFirestation().getId().equals(firestationId)).collect(Collectors.toList());
    }

    @Override
    public Firestation saveFirestation(Firestation firestation) {
        HashMap<Long, Firestation> firestationHashMap = JsonReader.getFirestations();
        HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
        if(firestation.getId() == null && firestation.getName() != null){
            firestation.setId(firestationHashMap.size() + 1L);
        }

        addressHashMap.values().forEach(address -> {
            if (address.getFirestation().getId().equals(firestation.getId()))
                address.getFirestation().setName(firestation.getName());
        });

        firestationHashMap.put(firestation.getId(), firestation);

        JsonReader.setAddresses(addressHashMap);
        JsonReader.setFirestations(firestationHashMap);
        return JsonReader.getFirestations().get(firestation.getId());
    }

    @Override
    public void deleteFirestation(Long id) {
        HashMap<Long, Firestation> firestationHashMap = JsonReader.getFirestations();
        HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
        addressHashMap.values().forEach(address -> {
            if(address.getFirestation() != null && address.getFirestation().getId().equals(id))
                address.setFirestation(null);
        });
        firestationHashMap.remove(id);
        JsonReader.setAddresses(addressHashMap);
        JsonReader.setFirestations(firestationHashMap);
    }
}
