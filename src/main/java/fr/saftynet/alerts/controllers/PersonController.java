package fr.saftynet.alerts.controllers;

import ch.qos.logback.classic.Logger;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PatientMedicineService patientMedicineService;

    Logger logger = (Logger) LoggerFactory.getLogger(PersonController.class);

    @PostMapping("/person")
    public Person createNewPerson(@RequestBody Person person){
        if(person.getId()==0){
            Person newPerson = personService.savePerson(person);
            List<PatientMedicine> patientMedicines = new ArrayList<>();
            person.getMedicines().forEach(patientMedicine -> {
                Medicine medicine = new Medicine();
                medicine.setId(patientMedicine.getMedicineId().getId());
                patientMedicine.setPersonId(newPerson);
                patientMedicine.setMedicineId(medicine);
                patientMedicineService.savePatientMedicine(patientMedicine);
                patientMedicines.add(patientMedicine);
            });
            newPerson.setMedicines(patientMedicines);
            return newPerson;
        }
        return null;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person){
        return personService.updatePerson(person);
    }

    @PutMapping("/person/{id}")
    public Person updatePersonId(@RequestBody Person person, @PathVariable Long id){
        if(id > 0){
            person.setId(id);
            return personService.updatePerson(person);
        }
        return null;
    }

    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable final long id){personService.delete(id);}
}
