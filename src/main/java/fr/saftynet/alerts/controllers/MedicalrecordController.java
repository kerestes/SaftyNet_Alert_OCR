package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.*;
import fr.saftynet.alerts.utilities.JsonDateSerlializer;
import fr.saftynet.alerts.utilities.JsonResponse;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class MedicalrecordController {

    @Autowired
    private PersonService personService;
    @Autowired
    private PatientMedicineService patientMedicineService;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private AllergyService allergyService;

    ObjectMapper mapper = JsonDateSerlializer.getInstance();

    private static final Logger logger = LogManager.getLogger(MedicalrecordController.class);

    @PostMapping("/addMedicine")
    public JsonNode addMedicine(@RequestBody final ObjectNode medicineRequest, final HttpServletRequest request){
        if(medicineRequest.has("name") && medicineRequest.has("dosage_mg")){
            Medicine medicine = new Medicine();
            medicine.setDosage_mg(medicineRequest.get("dosage_mg").asInt());
            medicine.setName(medicineRequest.get("name").asText());
            Optional<Medicine> optionalMedicine = Optional.ofNullable(medicineService.saveMedicine(medicine));
            if(optionalMedicine.isPresent()){
                logger.info("(POST) /addMedicine : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(optionalMedicine.get());
            }
            logger.error("(POST) /addMedicine : requests error -> There was an error to save medicine; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("There was an error to save medicine"));
        }
        logger.error("(POST) /addMedicine : requests error -> There is no name or dosage_mg in the request body; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("There is no name or dosage_mg in the request body"));
    }

    @PostMapping("/addAllergy")
    public JsonNode addAllergy(@RequestBody final ObjectNode medicineRequest, final HttpServletRequest request){
        if(medicineRequest.has("name")){
            Allergy allergy = new Allergy();
            allergy.setName(medicineRequest.get("name").asText());
            Optional<Allergy> optionalAllergy = Optional.ofNullable(allergyService.saveAllergy(allergy));
            if(optionalAllergy.isPresent()){
                logger.info("(POST) /addAllergy : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(optionalAllergy.get());
            }
            logger.error("(POST) /addAllergy : requests error -> There was an error to save allergy; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("There was an error to save medicine"));
        }
        logger.error("(POST) /addAllergy : requests error -> There is no name in the request body; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("There is no name in the request body"));
    }

    @PutMapping("/medicine/{personId}")
    public JsonNode addMedicineToPerson(@PathVariable final Long personId, @RequestBody final ObjectNode medicineRequest, final HttpServletRequest request){
        if(personId != null && personId > 0){
            int quantity = MedicalRecordUtility.setQuantity(medicineRequest);
            if(medicineRequest.has("medicineId")){
                Long realMedicineId = medicineRequest.get("medicineId").asLong();
                Optional<Medicine> medicine = medicineService.getMedicine(realMedicineId);
                Optional<Person> person = personService.getPerson(personId);
                if(person.isPresent() && medicine.isPresent()){
                    Optional<PatientMedicine> patientMedicine = patientMedicineService.getPatientMedicineByPersonId(person.get(), medicine.get());
                    patientMedicineService.savePatientMedicine(MedicalRecordUtility.updatePatientMedicine(patientMedicine, person.get(), medicine.get(), quantity));
                    logger.info("(PUT) /medicine/" + personId + ", request body -> medicineId = " + realMedicineId + ", quantity = " + quantity + ": request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                    return mapper.valueToTree(personService.getPerson(personId).get());
                }
                logger.error("(PUT) /medicine/" + personId + " : requests error -> Person (id=" + personId +") or Medicine (id=" + realMedicineId + ") does not exist; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(JsonResponse.errorResponse("Person (id=" + personId +") or Medicine (id=" + realMedicineId + ") does not exist"));
            }
            logger.error("(PUT) /medicine/" + personId + " : requests error -> Invalid Medicine id; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("Invalid Medicine id"));
        }
        logger.error("(PUT) /medicine/" + personId + " : requests error -> Invalid Person id; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid Person id"));
    }

    @PutMapping("/allergy/{personId}")
    public JsonNode addAllergyToPerson(@PathVariable final Long personId, @RequestBody final ObjectNode allergyRequest, final HttpServletRequest request){
        if(personId != null && personId > 0){
            if(allergyRequest.has("allergyId")){
                Long allergyId = allergyRequest.get("allergyId").asLong();
                Optional<Person> optionalPerson = personService.getPerson(personId);
                Optional<Allergy> optionalAllergy = allergyService.getAllergy(allergyId);
                if(optionalPerson.isPresent() && optionalAllergy.isPresent()){
                    logger.info("(PUT) /allergy/" + personId + ", request body -> allergyId = " + allergyId + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                    return mapper.valueToTree(personService.savePerson(MedicalRecordUtility.addAllergy(optionalPerson.get(), optionalAllergy.get())));
                }
                logger.error("(PUT) /allergy/" + personId + ", request error -> Person (id=" + personId +") or Allergy (id=" + allergyId + ") does not exist; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(JsonResponse.errorResponse("Person (id=" + personId +") or Allergy (id=" + allergyId + ") does not exist"));
            }
            logger.error("(PUT) /allergy/" + personId + ", request error -> Invalid Allergy id; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("Invalid Allergy id"));
        }
        logger.error("(PUT) /allergy/" + personId + ", request error -> Invalid Person id; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid Person id"));
    }

    @DeleteMapping("/medicine/{personId}/{medicineId}")
    public JsonNode deleteMedicineFromPerson(@PathVariable final Long personId, @PathVariable final Long medicineId, final HttpServletRequest request){
        if(personId != null && personId > 0){
            if(medicineId != null && medicineId > 0){
                patientMedicineService.deletePatientMedicine(personId, medicineId);
                logger.info("(DELETE) /medicine/" + personId + "/" + medicineId + " : request made successfully" );
                HashMap<String, String> deleteResponse = new HashMap<>();
                deleteResponse.put("Delete", "If the relationship between Person id (" + personId + ") and Medicine id (" + medicineId + ") exists, it was deleted");
                return mapper.valueToTree(deleteResponse);
            }
            logger.error("(DELETE) /medicine/" + personId + "/" + medicineId + " : Invalid Medicine Id" );
            return mapper.valueToTree(JsonResponse.errorResponse("Invalid Medicine Id"));
        }
        logger.error("(DELETE) /medicine/" + personId + "/" + medicineId + " : Invalid Person Id" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid Person Id"));
    }

    @DeleteMapping("/allergy/{personId}/{allergyId}")
    public JsonNode deleteAllergieFromPerson(@PathVariable final Long personId, @PathVariable final Long allergyId, final HttpServletRequest request){
        if(personId != null && personId > 0){
            if(allergyId != null && allergyId > 0){
                allergyService.deleteAllergyFromPerson(personId, allergyId);
                logger.info("(DELETE) /medicine/" + personId + "/" + allergyId + " : request made successfully" );
                HashMap<String, String> deleteResponse = new HashMap<>();
                deleteResponse.put("Delete", "If the relationship between Person id (" + personId + ") and Allergy id (" + allergyId + ") exists, it was deleted");
                return mapper.valueToTree(deleteResponse);
            }
            logger.error("(DELETE) /medicine/" + personId + "/" + allergyId + " : Invalid Allergy Id" );
            return mapper.valueToTree(JsonResponse.errorResponse("Invalid Allergy Id"));
        }
        logger.error("(DELETE) /medicine/" + personId + "/" + allergyId + " : Invalid Person Id" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid Person Id"));
    }

}
