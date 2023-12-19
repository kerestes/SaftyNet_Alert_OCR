package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.repositories.IAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private IAddressRepository addressRepository;

    public Optional<String> getCity(String city){return addressRepository.getCity(city);}
    public Optional<Address> getAddress(final Long id){return addressRepository.getAddress(id);}

    public List<Address> getPersonByLastName(final String lastName){return addressRepository.getPersonByLastName(lastName);}

    public Optional<Address> getAddressByName(final String addressName){return addressRepository.getAddressByName(addressName);}
}
