package fr.saftynet.alerts.utilities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MedicalRecordUtility {

    private static final Logger logger = LogManager.getLogger();

    public static List<PatientMedicine> createMedicalRecordForNewPerson(List<PatientMedicine> patientMedicines, Person person){
        logger.debug("Creating a medical record to " + person.getFirstName() + " " + person.getLastName());
        return patientMedicines.stream().map(patientMedicine -> {
            Medicine medicine = new Medicine();
            medicine.setId(patientMedicine.getMedicineId().getId());
            patientMedicine.setPersonId(person);
            patientMedicine.setMedicineId(medicine);
            return patientMedicine;
        }).collect(Collectors.toList());
    }
    public static PatientMedicine addMedicine(int quantity, Person person, Medicine medicine){
        logger.debug("Connecting person ("+ person.getFirstName() + " " + person.getLastName() +") and medicine ("+ medicine.getName() +")");
        PatientMedicine newPatientMedicine = new PatientMedicine();
        newPatientMedicine.setQuantity(quantity);
        newPatientMedicine.setPersonId(person);
        newPatientMedicine.setMedicineId(medicine);
        return newPatientMedicine;
    }

    public static Person addAllergy(Person person, Allergy allergy){
        logger.debug("Adding allergy (" + allergy.getName() + ") to person (" + person.getFirstName() + " " + person.getLastName() + ")");
        if(!person.getAllergies().contains(allergy))
            person.getAllergies().add(allergy);
        return person;
    }

    public static Person setNullMecialRecord(Person person){
        logger.debug("Setting allergies and medicines to null");
        person.setAllergies(null);
        person.setMedicines(null);
        return person;
    }

    public static int setQuantity (ObjectNode medicineRequest){
        logger.debug("setting quantity of medicine for a person");
        if (medicineRequest.has("quantity") && medicineRequest.get("quantity").asInt() > 0)
            return medicineRequest.get("quantity").asInt();
        else
            return 1;
    }

    public static PatientMedicine updatePatientMedicine(Optional<PatientMedicine> patientMedicine, Person person, Medicine medicine, int quantity){
        logger.debug("updating a person's medications");
        PatientMedicine returnPatientMedicine = new PatientMedicine();
        if(patientMedicine.isPresent()){
            returnPatientMedicine = patientMedicine.get();
            returnPatientMedicine.setQuantity(quantity);
        }
        else{
            returnPatientMedicine = MedicalRecordUtility.addMedicine(quantity, person, medicine);
        }
        return returnPatientMedicine;
    }
}
