package fr.saftynet.alerts.repositories.jsonImpl;


import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.IPersonRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PersonRepositoryJsonImpl implements IPersonRepository {
    @Override
    public Optional<Person> getPerson(Long id) {
        return JsonReader.getAddresses().values().stream()
                .flatMap(address -> address.getPersons().stream())
                .filter(person -> person.getId().equals(id)).findFirst();
    }

    @Override
    public List<String> getEmailPerCity(String cityName) {
        Collection<Address> addresses = JsonReader.getAddresses().values();
        Set<String> emails = addresses.stream().flatMap(address -> address.getPersons().stream()).map(person -> person.getEmail()).collect(Collectors.toSet());
        return new ArrayList<>(emails);
    }

    @Override
    public Person savePerson(Person person) {
        HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
        HashMap<Long, Person> personHashMap = JsonReader.getPersons();
        Address address = addressHashMap.get(person.getAddress().getId());
        if(person.getId() == null){
            person.setId(personHashMap.size() + 1L);
            person.setAddress(address);
            if(!person.getAllergies().isEmpty()){
                List<Allergy> allergies = new ArrayList<>();
                person.getAllergies().forEach(allergy -> allergies.add(JsonReader.getAllergies().get(allergy.getId())));
                person.setAllergies(allergies);
            }
        }
        personHashMap.put(person.getId(), person);
        JsonReader.setPersons(personHashMap);
        List<Person> personList = address.getPersons().stream()
                        .filter(person1 -> !person1.getId().equals(person.getId())).collect(Collectors.toList());
        address.setPersons(personList);
        address.getPersons().add(person);
        addressHashMap.put(address.getId(), address);
        JsonReader.setAddresses(addressHashMap);
        return JsonReader.getPersons().get(person.getId());
    }

    @Override
    public void deletePerson(Long id) {
        Optional<Person> optionalPerson = getPerson(id);
        if(optionalPerson.isPresent()) {
            HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
            Address address = addressHashMap.get(optionalPerson.get().getAddress().getId());
            List<Person> personList = address.getPersons().stream()
                            .filter(person -> !person.getId().equals(id)).collect(Collectors.toList());
            address.setPersons(personList);
            addressHashMap.put(address.getId(), address);
            JsonReader.setAddresses(addressHashMap);
            HashMap<Long, Person> personHashMap = JsonReader.getPersons();
            personHashMap.remove(optionalPerson.get().getId());
            JsonReader.setPersons(personHashMap);
        }
    }
}
