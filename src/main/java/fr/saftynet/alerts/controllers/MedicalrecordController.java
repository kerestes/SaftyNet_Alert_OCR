package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.*;
import fr.saftynet.alerts.utilities.AddressUtility;
import fr.saftynet.alerts.utilities.MedicalRecordUtility;
import fr.saftynet.alerts.utilities.PersonUtility;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

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

    Logger logger = (Logger) LoggerFactory.getLogger(MedicalrecordController.class);

    @GetMapping("/fire")
    public Address getPersonByAddressWithMedicalRecords(@Param("address") String address){
        Optional<Address> optionalAddress = addressService.getAddressByName(address);
        if(optionalAddress.isPresent()){
            return AddressUtility.changeBirthdayForAge(optionalAddress.get());
        }
        return null;
    }

    @PutMapping("/medicine/{personId}")
    public Person addMedicine(@PathVariable Long personId, @RequestBody ObjectNode medicineRequest){
        int quantity;
        if (medicineRequest.has("quantity") && medicineRequest.get("quantity").asInt() > 0)
            quantity = medicineRequest.get("quantity").asInt();
        else
            quantity = 1;
        Long realMedicineId = medicineRequest.get("medicineId").asLong();
        Optional<Medicine> medicine = medicineService.getMedicine(realMedicineId);
        Optional<Person> person = personService.getPerson(personId);

        if(person.isPresent() && medicine.isPresent()){
            Optional<PatientMedicine> patientMedicine = patientMedicineService.getPatientMedicineByPersonId(person.get(), medicine.get());
            if(!patientMedicine.isPresent()){
                patientMedicineService.savePatientMedicine(MedicalRecordUtility.addMedicine(quantity, person.get(), medicine.get()));
            } else{
                patientMedicine.get().setQuantity(quantity);
                patientMedicineService.savePatientMedicine(patientMedicine.get());
            }
            return personService.getPerson(personId).get();
        }
        return null;
    }

    @PutMapping("/allergy/{personId}")
    public Person addAllergy(@PathVariable Long personId, @RequestBody ObjectNode allergyRequest){
        Optional<Person> optionalPerson = personService.getPerson(personId);
        Long allergyId = allergyRequest.get("allergyId").asLong();
        Optional<Allergy> optionalAllergy = allergyService.getAllergy(allergyId);
        if(optionalPerson.isPresent() && optionalAllergy.isPresent()){
            return personService.savePerson(MedicalRecordUtility.addAllergie(optionalPerson.get(), optionalAllergy.get(), allergyId));
        }
        return null;
    }

    @DeleteMapping("/medicine/{personId}/{medicineId}")
    public void deleteMedicine(@PathVariable Long personId, @PathVariable Long medicineId){
        patientMedicineService.deletePatientMedicine(personId, medicineId);
    }

    @DeleteMapping("/allergy/{personId}/{allergyId}")
    public void deleteAllergie(@PathVariable Long personId, @PathVariable Long allergyId){
        allergyService.deleteAllergy(personId, allergyId);
    }

}
