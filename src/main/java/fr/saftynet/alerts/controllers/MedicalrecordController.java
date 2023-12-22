package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.*;
import fr.saftynet.alerts.utilities.JsonDateSerlializer;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        }
        return null;
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
        }
        return null;
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
            }
        }
        return null;
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
            }
        }
        return null;
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
        }
        return null;
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
        }
        return null;
    }

}
