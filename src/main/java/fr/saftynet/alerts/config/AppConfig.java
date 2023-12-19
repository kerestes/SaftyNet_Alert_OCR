package fr.saftynet.alerts.config;

import fr.saftynet.alerts.repositories.*;
import fr.saftynet.alerts.repositories.dbImpl.*;
import fr.saftynet.alerts.repositories.jsonImpl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IAddressRepository addressRepository(){
        //return new AddressRepositoryDbImpl();
        return new AddressRepositoryJsonImpl();
    }

    @Bean
    public IAllergyRepository allergyRepository(){
        //return new AllergyRepositoryDbImpl();
        return new AllergyRepositoryJsonImpl();
    }

    @Bean
    public IMedicineRepository medicineRepository(){
        //return new MedicineRepositoryDbImpl();
        return new MedicineRepositoryJsonImpl();
    }

    @Bean
    public IFirestationRepository firestationRepository(){
        //return new FirestationRepositoryDbImpl();
        return new FirestationRepositoryJsonImpl();
    }

    @Bean
    public IPatientMedicineRepository patientMedicineRepository(){
        //return new PatientMedicineRepositoryDbImpl();
        return new PatientMedicineRepositoryJsonImpl();
    }

    @Bean
    public IPersonRepository personRepository(){
        //return new PersonRepositoryDbImpl();
        return new PersonRepositoryJsonImpl();
    }
}
