package fr.saftynet.alerts.controllers;

import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/person")
    public Person createNewPerson(@RequestBody Person person){
        if(person.getId()==0)
            return personService.savePerson(person);
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
