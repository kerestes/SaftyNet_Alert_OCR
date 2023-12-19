package fr.saftynet.alerts.repositories.dbImpl;

import fr.saftynet.alerts.constans.DBConstants;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.repositories.IAddressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class AddressRepositoryDbImpl implements IAddressRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public Optional<String> getCity(String city) {
        Query query = em.createQuery(DBConstants.getCity);
        query.setParameter("city", "%"+city+"%");
        List<String> resultList = query.getResultList();
        if(resultList.isEmpty())
            return Optional.empty();
        else
            return Optional.ofNullable(resultList.get(0));
    }

    @Override
    public Optional<Address> getAddressByName(String addressName) {
        Query query = em.createQuery(DBConstants.getAddressByName);
        query.setParameter("addressName", "%"+addressName+"%");
        List<Address> resultList = query.getResultList();
        if(resultList.isEmpty())
            return Optional.empty();
        else
            return Optional.ofNullable(resultList.get(0));
    }

    @Override
    public List<Address> getPersonByLastName(String lastName) {
        Query query = em.createQuery(DBConstants.getPersonByLastName);
        query.setParameter("lastName", "%"+lastName+"%");
        List<Address> result = query.getResultList();
        return result;
    }

    @Override
    public Optional<Address> getAddress(Long id) {
        Optional<Address> optionalAddress = Optional.ofNullable(em.find(Address.class, id));
        return optionalAddress;
    }

    @Transactional
    @Override
    public Address updateAddress(Address address) {
        em.merge(address);
        em.flush();
        return getAddress(address.getId()).get();
    }

}
