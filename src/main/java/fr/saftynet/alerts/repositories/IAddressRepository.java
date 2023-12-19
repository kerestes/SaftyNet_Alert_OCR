package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.models.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressRepository {
    Optional<String> getCity(final String city);
    Optional<Address> getAddressByName(final String addressName);
    List<Address> getPersonByLastName(final String lastName);
    Optional<Address> getAddress(final Long id);
    Address updateAddress(Address address);
}
