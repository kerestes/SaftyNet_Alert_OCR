package fr.saftynet.alerts.controllers;

import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person){return personService.savePerson(person);}

    @PostMapping("/person")
    public Person createNewPerson(@RequestBody Person person){return personService.savePerson(person);}

    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable final long id){personService.delete(id);}
}
