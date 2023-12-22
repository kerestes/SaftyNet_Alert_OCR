package fr.saftynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Firestation;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.FirestationUtility;
import fr.saftynet.alerts.utilities.JsonDateSerlializer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AlertsController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private PersonService personService;

    @Autowired
    private FirestationService firestationService;

    private ObjectMapper mapper = JsonDateSerlializer.getInstance();

    private static final Logger logger = LogManager.getLogger(AlertsController.class);


    @GetMapping("/personInfo")
    public JsonNode getPersonInfoByLastName(@Param("lastName") final String lastName, final HttpServletRequest request){
        List<Address> addresses = addressService.getPersonByLastName(lastName);
        if (!addresses.isEmpty()){
            logger.info("(GET) /personInfo?lastName=" + lastName + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(AddressUtility.removeLastName(addresses, lastName));
        }
        return null;
    }

    @GetMapping("/communityEmail")
    public JsonNode getEmailPerCity(@Param("city") final String city, final HttpServletRequest request){
        Optional<String> cityName = addressService.getCity(city);
        if(cityName.isPresent()){
            logger.info("(GET) /communityEmail?city=" + city + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(cityName.map(s -> Map.of("Emails from " + s, personService.getEmailPerCity(s))).orElse(null));
        }
        return null;
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
        }
        return null;
    }

    @GetMapping("/fire")
    public JsonNode getPersonByAddressWithMedicalRecords(@Param("address") final String address, HttpServletRequest request){
        Optional<Address> optionalAddress = addressService.getAddressByName(address);
        if(optionalAddress.isPresent()){
            logger.info("(GET) /fire?address=" + address + " : request made successfully; Made by (" + request.getRemoteAddr() + ")" );
            return mapper.valueToTree(AddressUtility.changeBirthdayForAge(optionalAddress.get()));
        }
        return null;
    }

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
        }
        return null;
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
        }
        return null;
    }
}
