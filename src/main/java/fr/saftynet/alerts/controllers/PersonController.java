package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.PersonUtility;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private PatientMedicineService patientMedicineService;
    @Autowired
    private AddressService addressService;

    Logger logger = (Logger) LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/personInfo")
    public List<Address> getPersonInfoByLastName(@Param("lastName") String lastName){
        final String finalLastName = lastName.toUpperCase();
        List<Address> addresses = addressService.getPersonByLastName(lastName);
        for(Address address: addresses){
            address.getPersons().removeIf(person -> !person.getLastName().contains(finalLastName));
            for(Person person: address.getPersons()){
                person.setAge(PersonUtility.getAge(person.getBirthday()));
                person.setBirthday(null);
                person.setPhone(null);
            }
        }
        return addresses;
    }

    @GetMapping("/communityEmail")
    public List<String> getEmailPerCity(@Param("city") String city){
        return personService.getEmailPerCity(city);
    }

    @GetMapping("/childAlert")
    public Address getChildrenByAddress(@Param("address") String address){
        Optional<Address> optionalAddress = addressService.getAddressByName(address);
        if (optionalAddress.isPresent()){
            Address realAddress = optionalAddress.get();
            realAddress.setMinor(new ArrayList<>());
            realAddress.setMajor(new ArrayList<>());
            for(Person person: realAddress.getPersons()){
                int age = PersonUtility.getAge(person.getBirthday());
                person.setBirthday(null);
                person.setEmail(null);
                person.setAllergies(null);
                person.setMedicines(null);
                if (age > 18)
                    realAddress.getMinor().add(person);
                else
                    realAddress.getMajor().add(person);
            }
            if(realAddress.getMinor().isEmpty())
                return null;
            realAddress.setPersons(null);
            return realAddress;
        }
        return null;
    }

    @PostMapping("/person")
    public Person createNewPerson(@RequestBody Person person){
        if(person.getId()==0){
            person.setLastName(person.getLastName().toUpperCase());
            Person newPerson = personService.savePerson(person);
            List<PatientMedicine> patientMedicines = new ArrayList<>();
            person.getMedicines().forEach(patientMedicine -> {
                Medicine medicine = new Medicine();
                medicine.setId(patientMedicine.getMedicineId().getId());
                patientMedicine.setPersonId(newPerson);
                patientMedicine.setMedicineId(medicine);
                patientMedicineService.savePatientMedicine(patientMedicine);
                patientMedicines.add(patientMedicine);
            });
            newPerson.setMedicines(patientMedicines);
            return newPerson;
        }
        return null;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person){
        Optional<Person> optinalPerson = personService.getPerson(person.getId());
        if(optinalPerson.isPresent()) {
            person = PersonUtility.createUpdate(person, optinalPerson.get());
            return personService.savePerson(person);
        }
        return null;
    }

    @PutMapping("/person/{id}")
    public Person updatePersonId(@RequestBody Person person, @PathVariable Long id){
        person.setId(id);
        return updatePerson(person);
    }

    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable final long id){personService.delete(id);}
}
