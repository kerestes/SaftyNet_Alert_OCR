package fr.saftynet.alerts.repositories.jsonImpl;

import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.repositories.IAddressRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AddressRepositoryJsonImpl implements IAddressRepository {
    @Override
    public Optional<String> getCity(String city) {
        Collection<Address> addresses = JsonReader.getAddresses().values();
        Optional<Address> optionalAddress = addresses.stream().filter(address -> address.getCity().toLowerCase().contains(city.toLowerCase())).findFirst();
        if(optionalAddress.isPresent())
            return Optional.ofNullable(optionalAddress.get().getCity());
        return Optional.empty();
    }

    @Override
    public Optional<Address> getAddressByName(String addressName) {
        Collection<Address> addresses = JsonReader.getAddresses().values();
        return addresses.stream().filter(address -> address.getAddress().toLowerCase().contains(addressName.toLowerCase())).findFirst();
    }

    @Override
    public List<Address> getPersonByLastName(String lastName) {
        final String lastNameUpper = lastName.toUpperCase();
        List<Address> finalAddresses = new ArrayList<>();
        Collection<Address> addresses = JsonReader.getAddresses().values();
        addresses.forEach(address -> {
            if(address.getPersons().stream().anyMatch(person -> person.getLastName().contains(lastNameUpper))){
                finalAddresses.add(address);
            }
        });
        return finalAddresses;
    }

    @Override
    public Optional<Address> getAddress(Long id) {
        return Optional.ofNullable(JsonReader.getAddresses().get(id));
    }

    @Override
    public Address updateAddress(Address address) {
        HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
        addressHashMap.put(address.getId(), address);
        JsonReader.setAddresses(addressHashMap);
        return address;
    }
}
