package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import fr.saftynet.alerts.utilities.PersonUtility;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        List<Address> addresses = addressService.getPersonByLastName(lastName);
        if (!addresses.isEmpty())
            return AddressUtility.removeLastName(addresses, lastName);
        return null;
    }

    @GetMapping("/communityEmail")
    public Map<String, List<String>> getEmailPerCity(@Param("city") String city){
        Optional<String> cityName = addressService.getCity(city);
        return cityName.map(s -> Map.of("Emails from " + s, personService.getEmailPerCity(s))).orElse(null);
    }

    @GetMapping("/childAlert")
    public Address getChildrenByAddress(@Param("address") String address){
        Optional<Address> optionalAddress = addressService.getAddressByName(address);
        if (optionalAddress.isPresent()){
            Address addresses = AddressUtility.setMinorAndMajorList(Arrays.asList(optionalAddress.get())).get(0);
            if(!addresses.getMinor().isEmpty())
                return addresses;
        }
        return null;
    }

    @PostMapping("/person")
    public Person createNewPerson(@RequestBody Person person){
        if(person.getId() == null){
            person.setLastName(person.getLastName().toUpperCase());
            Person newPerson = personService.savePerson(person);
            if (newPerson != null){
                List<PatientMedicine> patientMedicines = MedicalRecordUtility.createMedicalRecordForNewPerson(person.getMedicines(), newPerson);
                patientMedicines.forEach(patientMedicine -> patientMedicineService.savePatientMedicine(patientMedicine));
                return personService.getPerson(newPerson.getId()).get();
            }
        }
        return null;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person){
        if(person.getId() != null){
            Optional<Person> optinalPerson = personService.getPerson(person.getId());
            if(optinalPerson.isPresent()) {
                person = PersonUtility.createUpdate(person, optinalPerson.get());
                return personService.savePerson(person);
            }
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
