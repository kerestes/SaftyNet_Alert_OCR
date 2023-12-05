package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.*;
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
            Address realAddress = optionalAddress.get();
            for (Person person: realAddress.getPersons()){
                person.setAge(PersonUtility.getAge(person.getBirthday()));
                person.setBirthday(null);
                person.setEmail(null);
            }
            return realAddress;
        }
        return null;
    }

    @PutMapping("/medicine/{personId}")
    public Person addMedicine(@PathVariable Long personId, @RequestBody JsonNode medicineRequest){
        int quantity;
        Long realMedicineId = medicineRequest.get("medicineId").asLong();
        Optional<Person> person = personService.getPerson(personId);
        Optional<Medicine> medicine = medicineService.getMedicine(realMedicineId);
        if(person.isPresent() && medicine.isPresent()){
            PatientMedicine patientMedicine = patientMedicineService.getPatientMedicineByPersonId(person.get(), medicine.get());

            if(medicineRequest.has("quantity") || medicineRequest.get("quantity").asInt() != 0)
                quantity = medicineRequest.get("quantity").asInt();
            else
                quantity = 1;

            if(patientMedicine != null && patientMedicine.getMedicineId().getId() == medicine.get().getId()) {
                patientMedicine.setQuantity(quantity);
            } else {
                PatientMedicine newPatientMedicine = new PatientMedicine();
                newPatientMedicine.setQuantity(quantity);
                newPatientMedicine.setPersonId(person.get());
                newPatientMedicine.setMedicineId(medicine.get());
                patientMedicineService.savePatientMedicine(newPatientMedicine);
            }

            return personService.getPerson(personId).get();
        }
        return null;
    }

    @PutMapping("/allergy/{personId}")
    public Person addAllergy(@PathVariable Long personId, @RequestBody JsonNode allergyRequest){
        Optional<Person> optionalPerson = personService.getPerson(personId);
        Long allergyId = allergyRequest.get("allergyId").asLong();
        if(optionalPerson.isPresent()){
            Allergy newAllergy = new Allergy();
            newAllergy.setId(allergyId);
            Person p = optionalPerson.get();
            Optional<Allergy> allergy = allergyService.getAllergy(allergyId);
            if(allergy.isPresent() && p.getAllergies().contains(allergy.get()))
                return p;
            p.getAllergies().add(allergy.get());
            return personService.savePerson(p);
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
