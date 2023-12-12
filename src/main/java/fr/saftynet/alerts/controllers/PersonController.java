package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.JsonResponse;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import fr.saftynet.alerts.utilities.PersonUtility;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LogManager.getLogger();

    @PostConstruct
    private void setDateFormat(){
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    @GetMapping("/personInfo")
    public JsonNode getPersonInfoByLastName(@Param("lastName") final String lastName, final HttpServletRequest request){
        List<Address> addresses = addressService.getPersonByLastName(lastName);
        if (!addresses.isEmpty()){
            logger.info("(GET) /personInfo?lastName=" + lastName + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(AddressUtility.removeLastName(addresses, lastName));
        }
        logger.error("(GET) /personInfo?lastName=" + lastName + " : requests error -> There is no person named " + lastName + "; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("There is no person named " + lastName));
    }

    @GetMapping("/communityEmail")
    public JsonNode getEmailPerCity(@Param("city") final String city, final HttpServletRequest request){
        Optional<String> cityName = addressService.getCity(city);
        if(cityName.isPresent()){
            logger.info("(GET) /communityEmail?city=" + city + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(cityName.map(s -> Map.of("Emails from " + s, personService.getEmailPerCity(s))).orElse(null));
        }
        logger.error("(GET) /communityEmail?city=" + city + " : requests error -> There is no city named " + city + "; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("There is no city named " + city));
    }

    @GetMapping("/childAlert")
    public JsonNode getChildrenByAddress(@Param("address") final String address, final HttpServletRequest request){
        Optional<Address> optionalAddress = addressService.getAddressByName(address);
        if (optionalAddress.isPresent()){
            Address preparedAddress = AddressUtility.setMinorAndMajorList(Arrays.asList(optionalAddress.get())).get(0);
            if(!preparedAddress.getMinor().isEmpty()){
                logger.info("(GET) /childAlert?address=" + address + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(preparedAddress);
            }
            logger.error("(GET) /childAlert?address=" + address + " : requests error -> There is no minor in " + preparedAddress.getAddress() + "; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("There is no minor in " + preparedAddress.getAddress()));
        }
        logger.error("(GET) /childAlert?address=" + address + " : requests error -> The address " + address + " does not exists; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("The address " + address + " does not exists"));
    }

    @PostMapping("/person")
    public JsonNode createNewPerson(@RequestBody Person person, final HttpServletRequest request){
        if (person.getAddress() != null
                && person.getFirstName() != null && !person.getFirstName().isEmpty()
                && person.getLastName() != null && !person.getLastName().isEmpty()
                && person.getPhone() != null && !person.getPhone().isEmpty()
                && person.getEmail() != null && !person.getEmail().isEmpty()
                && person.getBirthday() != null ){

            person.setLastName(person.getLastName().toUpperCase());
            Person newPerson = personService.savePerson(person);
            if (newPerson != null){
                List<PatientMedicine> patientMedicines = MedicalRecordUtility.createMedicalRecordForNewPerson(person.getMedicines(), newPerson);
                patientMedicines.forEach(patientMedicine -> patientMedicineService.savePatientMedicine(patientMedicine));
                logger.info("(POST) /person : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(personService.getPerson(newPerson.getId()).get());
            }
            logger.error("(POST) /person : requests error -> There was an error to save de Person; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("There was an error to save de Person"));
        }
        logger.error("(POST) /person : requests error -> There is a missing attribute, make sure you entered at least (firstName, lastName, phone, email, birthday and address{id}); Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("There is missing attribute, make sure you entered at least (firstName, lastName, phone, email, birthday and address{id})"));
    }

    @PutMapping("/person")
    public JsonNode updatePerson(@RequestBody Person person, final HttpServletRequest request){
        if(person.getId() != null){
            Optional<Person> optinalPerson = personService.getPerson(person.getId());
            if(optinalPerson.isPresent()) {
                person = PersonUtility.createUpdate(person, optinalPerson.get());
                logger.info("(PUT) /person : request body -> id = " + person.getId() + ": request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(personService.savePerson(person));
            }
            logger.error("(PUT) /person : requests error -> There is no person with this id; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("There is no person with this id"));
        }
        logger.error("(PUT) /person : requests error -> The Person's id must not be null; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("The Person's id must not be null"));
    }

    @PutMapping("/person/{id}")
    public JsonNode updatePersonId(@RequestBody Person person, @PathVariable final Long id, final HttpServletRequest request){
        person.setId(id);
        return updatePerson(person, request);
    }

    @DeleteMapping("/person/{id}")
    public JsonNode deletePerson(@PathVariable final Long id, final HttpServletRequest request){
        if(id != null && id > 0){
            logger.info("(DELETE) /person/" + id + ": request made successfully;Made by (" + request.getRemoteAddr() + ")" );
            personService.delete(id);
            return mapper.valueToTree(JsonResponse.deleteResponse(id));
        }
        logger.error("(DELETE) /person/" + id + ": requests error -> Invalid ID ;Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid ID"));
    }
}
