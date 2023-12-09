package fr.saftynet.alerts.utilities;

import fr.saftynet.alerts.models.Allergy;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordUtility {

    public static List<PatientMedicine> createMedicalRecordForNewPerson(List<PatientMedicine> patientMedicines, Person person){
        return patientMedicines.stream().map(patientMedicine -> {
            Medicine medicine = new Medicine();
            medicine.setId(patientMedicine.getMedicineId().getId());
            patientMedicine.setPersonId(person);
            patientMedicine.setMedicineId(medicine);
            return patientMedicine;
        }).collect(Collectors.toList());
    }
    public static PatientMedicine addMedicine(int quantity, Person person, Medicine medicine){
        PatientMedicine newPatientMedicine = new PatientMedicine();
        newPatientMedicine.setQuantity(quantity);
        newPatientMedicine.setPersonId(person);
        newPatientMedicine.setMedicineId(medicine);
        return newPatientMedicine;
    }

    public static Person addAllergie(Person person, Allergy allergy){
        if(!person.getAllergies().contains(allergy))
            person.getAllergies().add(allergy);
        return person;
    }

    public static Person setNullMecialRecord(Person person){
        person.setAllergies(null);
        person.setMedicines(null);
        return person;
    }
}
