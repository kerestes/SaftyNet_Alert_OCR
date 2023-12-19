package fr.saftynet.alerts.repositories.jsonImpl;

import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.IAllergyRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AllergyRepositoryJsonImpl implements IAllergyRepository {
    @Override
    public Optional<Allergy> getAllergy(Long id) {
        return Optional.ofNullable(JsonReader.getAllergies().get(id));
    }

    @Override
    public void deleteAllergyFromPerson(Long personId, Long allergyId) {
        HashMap<Long, Person> personHashMap = JsonReader.getPersons();
        Person person = personHashMap.get(personId);
        if(person != null){
            Optional<Allergy> optionalAllergy = person.getAllergies().stream().filter(allergy -> allergy.getId().equals(allergyId)).findFirst();
            if (optionalAllergy.isPresent()) {
                HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();

                Address address = addressHashMap.get(person.getAddress().getId());
                List<Person> personList = address.getPersons().stream()
                                .filter(person1 -> !person1.getId().equals(personId)).collect(Collectors.toList());
                address.setPersons(personList);

                person.getAllergies().remove(optionalAllergy.get());
                personHashMap.put(person.getId(), person);

                address.getPersons().add(person);
                addressHashMap.put(address.getId(), address);

                JsonReader.setAddresses(addressHashMap);
                JsonReader.setPersons(personHashMap);
            }
        }
    }

    @Override
    public Allergy saveAllergy(Allergy allergy) {
        if(allergy.getId() == null && allergy.getName() != null){
            HashMap<Long, Allergy> allergyHashMap = JsonReader.getAllergies();
            allergy.setId(allergyHashMap.size() + 1L);
            allergyHashMap.put(allergy.getId(), allergy);
            JsonReader.setAllergies(allergyHashMap);
        }
        return JsonReader.getAllergies().get(allergy.getId());
    }
}
