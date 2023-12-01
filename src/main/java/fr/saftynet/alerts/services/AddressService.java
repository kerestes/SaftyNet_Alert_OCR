package fr.saftynet.alerts.services;

import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Optional<Address> getAddress(final Long id){return addressRepository.findById(id);}
}
