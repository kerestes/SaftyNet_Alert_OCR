package fr.saftynet.alerts.repositories;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query(value = DBConstants.getAddressByName, nativeQuery = true)
    Optional<Address> getAddressByName(final String addressName);

    @Query(DBConstants.getPersonByLastName)
    List<Address> getPersonByLastName(final String lastName);
}
