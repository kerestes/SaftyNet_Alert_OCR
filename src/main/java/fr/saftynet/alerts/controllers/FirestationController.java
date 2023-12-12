package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.JsonResponse;
import fr.saftynet.alerts.utilities.FirestationUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;
    @Autowired
    private AddressService addressService;

    ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LogManager.getLogger();

    @GetMapping("/phoneAlert")
    public JsonNode getPhoneNumberPerFirestation(@Param("firestationId") final Long firestationId, final HttpServletRequest request){
        if(firestationId != null){
            Optional<Firestation> realFirestation = firestationService.getFirestation(firestationId);
            if(realFirestation.isPresent()){
                HashMap<String, Set<String>> returnHashMap = new HashMap<>();
                returnHashMap.put(realFirestation.get().getName(),  FirestationUtility.getPhones(firestationService.getPersonsPerFirestation(firestationId)));
                logger.info("(GET) /phoneAlert?firestationId=" + firestationId + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(returnHashMap);
            }

        } else {
            logger.error("(GET) /phoneAlert?firestationId=" + firestationId + " : requests error -> Firestation id must not be null; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("Firestation id must not be null"));
        }
        logger.error("(GET) /phoneAlert?firestationId=" + firestationId + " : requests error -> Firestation does not exist; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Firestation does not exist"));
    }

    @GetMapping("/firestation")
    public JsonNode getListPerson(@Param("stationNumber") final Long stationNumber, final HttpServletRequest request){
        if(stationNumber != null){
        ObjectMapper mapper = new ObjectMapper();
            List<Address> addresses = firestationService.getPersonsPerFirestation(stationNumber);
            if(!addresses.isEmpty()){
                logger.info("(GET) /firestation?stationNumber=" + stationNumber + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(AddressUtility.setMinorAndMajorList(addresses));
            }
        } else {
            logger.error("(GET) /phoneAlert?firestationId=" + stationNumber + " : requests error -> StationNumber must not be null; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("StationNumber must not be null"));
        }
        logger.error("(GET) /firestation?stationNumber=" + stationNumber + " : requests error -> Firestation does not exist; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Firestation does not exist"));
    }

    @PostMapping("/firestation")
    public JsonNode createFirestation(@RequestBody final ObjectNode firestation, final HttpServletRequest request){
        if(firestation.has("name")) {
            Firestation saveFirestation = new Firestation();
            saveFirestation.setName(firestation.get("name").asText());
            saveFirestation = firestationService.saveFirestation(saveFirestation);
            if(saveFirestation != null){
                logger.info("(POST) /firestation : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(saveFirestation);
            }
        }
        logger.error("(POST) /firestation : requests error -> Name field is missing in the request body; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Name field is missing in the request body"));
    }

    @PutMapping("/firestation")
    public JsonNode updateFirestation(@RequestBody final ObjectNode firestation, final HttpServletRequest request){
        if(firestation.has("id") && firestation.get("id").asLong() > 0){
            if(firestation.has("name")){
                Optional<Firestation> updateFirestation = firestationService.getFirestation(firestation.get("id").asLong());
                if(updateFirestation.isPresent()) {
                    Firestation savedFirestation = firestationService.saveFirestation(updateFirestation.get());
                    logger.info("(PUT) /firestation : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                    return mapper.valueToTree(savedFirestation);
                }
                logger.error("(PUT) /firestation : requests error -> Firestation does not exists; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(JsonResponse.errorResponse("Firestation does not exists"));
            }
            logger.error("(PUT) /firestation : requests error -> Name field is missing in the request body; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("Name field is missing in the request body"));
        }
        logger.error("(PUT) /firestation : requests error -> Id field is missing in the request body; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Id field is missing in the request body"));
    }

    @PutMapping("/firestation/{id}")
    public JsonNode updateFirestationId(@RequestBody ObjectNode firestation, @PathVariable final Long id, final HttpServletRequest request){
        firestation.put("id", id);
        return updateFirestation(firestation, request);
    }

    @PutMapping("/firestation/toaddress/{id}")
    public JsonNode addFirestationToAddress(@RequestBody ObjectNode objectNode, @PathVariable final Long id, final HttpServletRequest request){
        if(objectNode.has("firestationId")){
            Long firestationId = objectNode.get("firestationId").asLong();
            Optional<Firestation> firestation = firestationService.getFirestation(firestationId);
            if(firestation.isPresent()) {
                Optional<Address> realAddress = addressService.getAddress(id);
                if(realAddress.isPresent()){
                    realAddress.get().setFirestation(firestation.get());
                    Address address = firestationService.addFirestationToAddress(realAddress.get());
                    address.setPersons(null);
                    logger.info("(PUT) /firestation/toaddress/" + id + ", request body -> firestationId = " + firestationId + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
                    return mapper.valueToTree(address);
                }
                logger.error("(PUT) /firestation/toaddress/" + id + " : requests error -> There is no address with this id : " + id + "; Made by (" + request.getRemoteAddr() + ")" );
                return mapper.valueToTree(JsonResponse.errorResponse("There is no address with this id"));
            }
            logger.error("(PUT) /firestation/toaddress/" + id + " : requests error -> There is no firestaion with this id : "+ firestationId +"; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.errorResponse("There is no address with this id"));
        }
        logger.error("(PUT) /firestation/toaddress/" + id + " : requests error -> firestationId field is missing in the request body; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("firestationId field is missing in the request body"));
    }

    @DeleteMapping("/firestation/{id}")
    public JsonNode deleteFirestation(@PathVariable final Long id, final HttpServletRequest request){
        if (id != null && id>0){
            firestationService.deleteFirestation(id);
            logger.info("(DELETE) /firestation/" + id + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(JsonResponse.deleteResponse(id));
        }
        logger.error("(DELETE) /firestation/" + id + " : requests error -> Invalid id; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid id"));
    }

    @DeleteMapping("/firestation/toaddress/{id}")
    public JsonNode deleteMappingFirestation(@PathVariable final Long id, final HttpServletRequest request){
        if(id != null && id>0){
            logger.info("(DELETE) /firestation/toaddress/" + id + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            firestationService.deleteMappingFirestation(id);
            return mapper.valueToTree(JsonResponse.deleteResponse(id));
        }
        logger.error("(DELETE) /firestation/" + id + " : requests error -> Invalid id; Made by (" + request.getRemoteAddr() + ")" );
        return mapper.valueToTree(JsonResponse.errorResponse("Invalid id"));
    }

}
