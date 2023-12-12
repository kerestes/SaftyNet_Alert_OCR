package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.*;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.JsonDateSerlializer;
import fr.saftynet.alerts.utilities.JsonResponse;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @Autowired
    private AddressService addressService;

    ObjectMapper mapper = JsonDateSerlializer.getInstance();

    private static final Logger logger = LogManager.getLogger(MedicalrecordController.class);

    @GetMapping("/fire")
    public JsonNode getPersonByAddressWithMedicalRecords(@Param("address") final String address, HttpServletRequest request){
        Optional<Address> optionalAddress = addressService.getAddressByName(address);
        if(optionalAddress.isPresent()){
            logger.info("(GET) /fire?address=" + address + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(AddressUtility.changeBirthdayForAge(optionalAddress.get()));
        }
        logger.error("(GET) /fire?address=" + address + " : requests error -> There is no address named " + address + "; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("There is no address named " + address));
    }

    @PutMapping("/medicine/{personId}")
    public JsonNode addMedicine(@PathVariable final Long personId, @RequestBody final ObjectNode medicineRequest, final HttpServletRequest request){
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
                logger.error("(GET) /medicine/" + personId + " : requests error -> Person (id=" + personId +") or Medicine (id=" + realMedicineId + ") does not exist; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(JsonResponse.errorResponse("Person (id=" + personId +") or Medicine (id=" + realMedicineId + ") does not exist"));
            }
            logger.error("(GET) /medicine/" + personId + " : requests error -> Invalid Medicine id; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("Invalid Medicine id"));
        }
        logger.error("(GET) /medicine/" + personId + " : requests error -> Invalid Person id; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid Person id"));
    }

    @PutMapping("/allergy/{personId}")
    public JsonNode addAllergy(@PathVariable final Long personId, @RequestBody final ObjectNode allergyRequest, final HttpServletRequest request){
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
    public JsonNode deleteMedicine(@PathVariable final Long personId, @PathVariable final Long medicineId, final HttpServletRequest request){
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
    public JsonNode deleteAllergie(@PathVariable final Long personId, @PathVariable final Long allergyId, final HttpServletRequest request){
        if(personId != null && personId > 0){
            if(allergyId != null && allergyId > 0){
                allergyService.deleteAllergy(personId, allergyId);
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
