package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.*;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private PatientMedicineService patientMedicineService;

    private ObjectMapper mapper = JsonDateSerlializer.getInstance();

    private static final Logger logger = LogManager.getLogger(PersonController.class);




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
        if(person.getId() != null && person.getId() > 0){
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
