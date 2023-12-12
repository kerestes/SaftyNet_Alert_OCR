package fr.saftynet.alerts.unitaire.SpringContext;

import fr.saftynet.alerts.controllers.PersonController;
import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.PatientMedicineService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.AddressUtility;
import nl.altindag.log.LogCaptor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PersonService personService;
    @MockBean
    private PatientMedicineService patientMedicineService;
    @MockBean
    private AddressService addressService;

    @Test
    public void getPersonInfoByLastNameTest() throws Exception {
        when(addressService.getPersonByLastName(anyString())).thenReturn(makeListOfAddress());

        mockMvc.perform(get("/personInfo?lastName={lastName}", "boyd"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].address", containsStringIgnoringCase("1509 Culver St")),
                        jsonPath("$[1].address", containsStringIgnoringCase("112 Steppes Pl")),
                        jsonPath("$[0].persons", hasSize(5)),
                        jsonPath("$[1].persons", hasSize(1)),
                        jsonPath("$[0].persons[1].firstName", containsStringIgnoringCase("Jacob")),
                        jsonPath("$[0].persons[1].medicines", hasSize(3)),
                        jsonPath("$[0].persons[1].medicines[1].medicineId.name", is("terazine")),
                        jsonPath("$[1].persons[0].firstName", containsStringIgnoringCase("Allison")),
                        jsonPath("$[1].persons[0].allergies", hasSize(1)),
                        jsonPath("$[1].persons[0].allergies[0].name", is("nillacilan"))

                );

    }

    @Test
    public void getPersonInfoByLastNameNoAddressTest() throws Exception {
        when(addressService.getPersonByLastName(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/personInfo?lastName={lastName}", "boyd"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", containsStringIgnoringCase("There is no person named boyd"))

                );

    }

    @Test
    public void getEmailPerCityTest() throws Exception{

        when(addressService.getCity(anyString())).thenReturn(Optional.of("Culver"));
        when(personService.getEmailPerCity(anyString())).thenReturn(makeEmailList());

        mockMvc.perform(get("/communityEmail?city={city}", "Culver"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$['Emails from Culver']", hasSize(15)),
                        jsonPath("$['Emails from Culver'][0]", containsStringIgnoringCase("jaboyd@email.com")),
                        jsonPath("$['Emails from Culver']", hasItem("jpeter@email.com")),
                        jsonPath("$['Emails from Culver'][-1]", containsStringIgnoringCase("gramps@email.com"))
                );
    }

    @Test
    public void getEmailPerCityNullCityTest() throws Exception{

        when(addressService.getCity(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/communityEmail"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no city named null"))
                );
    }

    @Test
    public void getChildrenByAddressTest() throws Exception{
        when(addressService.getAddressByName(anyString())).thenReturn(makeUniqueAddress());


        mockMvc.perform(get("/childAlert?address={address}", "Downing")).
                andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(8)),
                        jsonPath("$.address", containsStringIgnoringCase("Downing")),
                        jsonPath("$.persons").doesNotExist(),
                        jsonPath("$.minor", hasSize(1)),
                        jsonPath("$.major", hasSize(2)),
                        jsonPath("$.minor[0].age", lessThan(18))
                );
    }

    @Test
    public void getChildrenByAddressNullAddressTest() throws Exception{
        when(addressService.getAddressByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/childAlert")).
                andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("The address null does not exists"))
                );
    }

    @Test
    public void getChildrenByAddressNoMinorTest() throws Exception{
        Address address = makeUniqueAddress().get();
        address = AddressUtility.setMinorAndMajorList(Arrays.asList(address)).get(0);
        address.setMinor(null);
        address.setPersons(new ArrayList<>());
        when(addressService.getAddressByName(anyString())).thenReturn(Optional.of(address));

        mockMvc.perform(get("/childAlert?address={address}", "Downing")).
                andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", containsStringIgnoringCase("There is no minor in"))
                );
    }

    @Test
    public void createNewPersonTest() throws Exception{

        when(personService.savePerson(any())).thenReturn(newPerson());
        when(patientMedicineService.savePatientMedicine(any())).thenReturn(null);
        when(personService.getPerson(any())).thenReturn(Optional.of(newPerson()));

        mockMvc.perform(post("/person").content("{" +
                        "    \"firstName\":\"Alexandre\"," +
                        "    \"lastName\":\"Kerestes\"," +
                        "    \"birthday\":\"1987-11-24\"," +
                        "    \"phone\":\"1234567890\"," +
                        "    \"email\":\"alexandrekerestes@exemplo.fr\"," +
                        "    \"address\":{\"id\":1}," +
                        "    \"allergies\":[" +
                        "        {\"id\":4}" +
                        "    ],\n" +
                        "    \"medicines\":[" +
                        "        {\"medicineId\":{\"id\":2}}," +
                        "        {\"medicineId\":{\"id\":3}}" +
                        "    ]" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.firstName", is("Alexandre")),
                        jsonPath("$.email", is("alexandrekerestes@exemplo.fr")),
                        jsonPath("$.allergies[0].id", is(4)),
                        jsonPath("$.medicines[0].quantity", is(1)),
                        jsonPath("$.medicines[0].medicineId.id", is(2))
                );
    }

    @Test
    public void createNewPersonMissintAttributeTest() throws Exception{

        mockMvc.perform(post("/person").content("{" +
                        "    \"firstName\":\"Alexandre\"," +
                        "    \"birthday\":\"1987-11-24\"," +
                        "    \"phone\":\"1234567890\"," +
                        "    \"email\":\"alexandrekerestes@exemplo.fr\"," +
                        "    \"address\":{\"id\":1}," +
                        "    \"allergies\":[" +
                        "        {\"id\":4}" +
                        "    ],\n" +
                        "    \"medicines\":[" +
                        "        {\"medicineId\":{\"id\":2}}," +
                        "        {\"medicineId\":{\"id\":3}}" +
                        "    ]" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is missing attribute, make sure you entered at least (firstName, lastName, phone, email, birthday and address{id})"))
                );
    }

    @Test
    public void createNewPersonInternalErrorTest() throws Exception{

        when(personService.savePerson(any())).thenReturn(null);

        mockMvc.perform(post("/person").content("{" +
                        "    \"firstName\":\"Alexandre\"," +
                        "    \"lastName\":\"Kerestes\"," +
                        "    \"birthday\":\"1987-11-24\"," +
                        "    \"phone\":\"1234567890\"," +
                        "    \"email\":\"alexandrekerestes@exemplo.fr\"," +
                        "    \"address\":{\"id\":1}," +
                        "    \"allergies\":[" +
                        "        {\"id\":4}" +
                        "    ],\n" +
                        "    \"medicines\":[" +
                        "        {\"medicineId\":{\"id\":2}}," +
                        "        {\"medicineId\":{\"id\":3}}" +
                        "    ]" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There was an error to save de Person"))
                );
    }

     @Test
     public void updatePersonTest() throws Exception{
        Person person = newPerson();
        person.setFirstName("Robert");
        when(personService.getPerson(anyLong())).thenReturn(Optional.of(newPerson()));
        when(personService.savePerson(any())).thenReturn(person);

        mockMvc.perform(put("/person").content("{\"id\":28, \"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.lastName", is("KERESTES")),
                        jsonPath("$.firstName", is("Robert"))
                );
     }

    @Test
    public void updatePersonNoIdTest() throws Exception{

        mockMvc.perform(put("/person").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("The Person's id must not be null"))
                );
    }

    @Test
    public void updatePersonNoPersonTest() throws Exception{
        when(personService.getPerson(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/person").content("{\"id\":28, \"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no person with this id"))
                );
    }

    @Test
    public void updatePersonIdTest() throws Exception{
        Person person = newPerson();
        person.setFirstName("Robert");
        when(personService.getPerson(anyLong())).thenReturn(Optional.of(newPerson()));
        when(personService.savePerson(any())).thenReturn(person);

        mockMvc.perform(put("/person/{id}", 28).content("{\"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.lastName", is("KERESTES")),
                        jsonPath("$.firstName", is("Robert"))
                );
    }

    @Test
    public void updatePersonIdNoIdTest() throws Exception{

        mockMvc.perform(put("/person/{id}", 0).content("{\"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("The Person's id must not be null"))
                );
    }

    @Test
    public void updatePersonIdNoPersonTest() throws Exception{
        when(personService.getPerson(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/person/{id}", 28).content("{\"firstName\":\"Robert\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("There is no person with this id"))
                );
    }

    @Test
    public void deletePersonTest() throws Exception {
        mockMvc.perform(delete("/person/{id}", 0))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.Error", is("Invalid ID"))
                );
    }

    private List<Address> makeListOfAddress(){

        Calendar calendar = Calendar.getInstance();

        Firestation firestation1 = new Firestation();
        firestation1.setId(3L);
        firestation1.setName("Firestation 3");

        Allergy allergy1 = new Allergy();
        allergy1.setId(3L);
        allergy1.setName("nillacilan");

        Medicine medicine1 = new Medicine();
        medicine1.setId(3L);
        medicine1.setName("anzol");
        medicine1.setDosage_mg(350);

        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setMedicineId(medicine1);
        patientMedicine1.setQuantity(1);

        Medicine medicine2 = new Medicine();
        medicine2.setId(5L);
        medicine2.setName("hydrapermazol");
        medicine2.setDosage_mg(100);

        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setQuantity(1);
        patientMedicine2.setMedicineId(medicine2);

        Person person1 = new Person();
        person1.setEmail("jaboyd@email.com");
        person1.setPhone("8418746512");
        calendar.set(1984, 3, 6);
        person1.setBirthday(calendar.getTime());
        person1.setFirstName("John");
        person1.setLastName("BOYD");
        person1.setAllergies(Arrays.asList(allergy1));
        person1.setMedicines(Arrays.asList(patientMedicine1, patientMedicine2));

        Medicine medicine3 = new Medicine();
        medicine3.setId(9L);
        medicine3.setName("pharmacol");
        medicine3.setDosage_mg(2500);

        PatientMedicine patientMedicine3 = new PatientMedicine();
        patientMedicine3.setMedicineId(medicine3);
        patientMedicine3.setQuantity(2);

        Medicine medicine4 = new Medicine();
        medicine4.setId(11L);
        medicine4.setName("terazine");
        medicine4.setDosage_mg(10);

        PatientMedicine patientMedicine4 = new PatientMedicine();
        patientMedicine4.setQuantity(1);
        patientMedicine4.setMedicineId(medicine4);

        Medicine medicine5 = new Medicine();
        medicine5.setId(8L);
        medicine5.setName("noznazol");
        medicine5.setDosage_mg(250);

        PatientMedicine patientMedicine5 = new PatientMedicine();
        patientMedicine5.setMedicineId(medicine5);
        patientMedicine5.setQuantity(1);

        Person person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("Jacob");
        person2.setLastName("BOYD");
        calendar.set(1989, 3, 6);
        person2.setBirthday(calendar.getTime());
        person2.setPhone("8418746513");
        person2.setEmail("drk@email.com");
        person2.setAllergies(new ArrayList<>());
        person2.setMedicines(Arrays.asList(patientMedicine3, patientMedicine4, patientMedicine5));

        Allergy allergy2 = new Allergy();
        allergy2.setId(4L);
        allergy2.setName("peanut");

        Person person3 = new Person();
        person3.setId(3L);
        person3.setFirstName("Tenley");
        person3.setLastName("BOYD");
        calendar.set(2012, 02, 18);
        person3.setBirthday(calendar.getTime());
        person3.setPhone("8418746512");
        person3.setEmail("tenz@email.com");
        person3.setAllergies(Arrays.asList(allergy2));
        person3.setMedicines(new ArrayList<>());

        Person person4 = new Person();
        person4.setId(4L);
        person4.setFirstName("Roger");
        person4.setLastName("BOYD");
        calendar.set(2017, 6, 9);
        person4.setBirthday(calendar.getTime());
        person4.setPhone("8418746512");
        person4.setEmail("jaboyd@email.com");
        person4.setAllergies(new ArrayList<>());
        person4.setMedicines(new ArrayList<>());

        Allergy allergy3 = new Allergy();
        allergy3.setName("xilliathal");
        allergy3.setId(6L);

        Medicine medicine6 = new Medicine();
        medicine6.setDosage_mg(650);
        medicine6.setName("tetracyclaz");
        medicine6.setId(10L);

        PatientMedicine patientMedicine6 = new PatientMedicine();
        patientMedicine6.setMedicineId(medicine6);
        patientMedicine6.setQuantity(1);

        Person person5 = new Person();
        person5.setFirstName("Felicia");
        person5.setLastName("BOYD");
        calendar.set(1986, 8, 1);
        person5.setBirthday(calendar.getTime());
        person5.setPhone("8418746544");
        person5.setEmail("jaboyd@email.com");
        person5.setAllergies(Arrays.asList(allergy3));
        person5.setMedicines(Arrays.asList(patientMedicine6));


        Address address1 = new Address();
        address1.setAddress("1509 Culver St");
        address1.setId(1L);
        address1.setZip("97451");
        address1.setCity("Culver");
        address1.setFirestation(firestation1);
        address1.setPersons(Arrays.asList(person1, person2, person3, person4, person5));

        Firestation firestation2 = new Firestation();
        firestation2.setName("Firestation 4");
        firestation2.setId(4L);

        Medicine medicine7 = new Medicine();
        medicine7.setId(2L);
        medicine7.setName("aznol");
        medicine7.setDosage_mg(200);

        PatientMedicine patientMedicine7 = new PatientMedicine();
        patientMedicine7.setQuantity(1);
        patientMedicine7.setMedicineId(medicine7);

        Person person6 = new Person();
        person6.setId(18L);
        person6.setFirstName("Allison");
        person6.setLastName("BOYD");
        calendar.set(1965, 3, 15);
        person6.setBirthday(calendar.getTime());
        person6.setPhone("8418749888");
        person6.setEmail("aly@imail.com");
        person6.setAllergies(Arrays.asList(allergy1));
        person6.setMedicines(Arrays.asList(patientMedicine7));

        Address address2 = new Address();
        address2.setAddress("112 Steppes Pl");
        address2.setId(6L);
        address2.setZip("97451");
        address2.setCity("Culver");
        address2.setFirestation(firestation2);
        address2.setPersons(Arrays.asList(person6));

        return Arrays.asList(address1, address2);
    }

    private List<String> makeEmailList(){
        return Arrays.asList(
                "jaboyd@email.com",
                "drk@email.com",
                "tenz@email.com",
                "clivfd@ymail.com",
                "tcoop@ymail.com",
                "jpeter@email.com",
                "aly@imail.com",
                "lily@email.com",
                "soph@email.com",
                "ward@email.com",
                "zarc@email.com",
                "reg@email.com",
                "bstel@email.com",
                "ssanw@email.com",
                "gramps@email.com"
        );
    }

    private Optional<Address> makeUniqueAddress(){
        Calendar calendar = Calendar.getInstance();
        Address address = new Address();

        Firestation firestation = new Firestation();
        firestation.setId(2L);
        firestation.setName("Firestation 2");

        Person person1 = new Person();
        person1.setId(12L);
        calendar.set(1988, 6, 3);
        person1.setBirthday(calendar.getTime());
        person1.setFirstName("Sophia");
        person1.setLastName("ZEMICKS");
        person1.setPhone("8418747878");
        person1.setEmail("soph@email.com");
        person1.setAllergies(new ArrayList<>());
        person1.setMedicines(new ArrayList<>());

        Person person2 = new Person();
        person2.setId(13L);
        person2.setFirstName("Warren");
        person2.setLastName("ZEMICKS");
        calendar.set(1985,3, 6);
        person2.setBirthday(calendar.getTime());
        person2.setPhone("8418747512");
        person2.setEmail("ward@email.com");
        person2.setAllergies(new ArrayList<>());
        person2.setMedicines(new ArrayList<>());

        Person person3 = new Person();
        person3.setId(14L);
        person3.setFirstName("Zach");
        person3.setLastName("ZEMICKS");
        person3.setPhone("8418747512");
        person3.setEmail("zarc@email.com");
        calendar.set(2017, 3, 6);
        person3.setBirthday(calendar.getTime());
        person3.setAllergies(new ArrayList<>());
        person3.setMedicines(new ArrayList<>());

        address.setId(8L);
        address.setAddress("892 Downing Ct");
        address.setZip("97451");
        address.setCity("Culver");
        address.setFirestation(firestation);
        address.setPersons(Arrays.asList(person1, person2, person3));

        return Optional.of(address);
    }

    public Person newPerson(){
        Calendar calendar = Calendar.getInstance();
        Person person = new Person();
        Allergy allergy = new Allergy();
        Medicine medicine1 = new Medicine();
        Medicine medicine2 = new Medicine();

        medicine1.setId(2L);
        medicine1.setName("aznol");
        medicine2.setId(3L);
        medicine2.setName("aznol");

        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setQuantity(1);
        patientMedicine1.setMedicineId(medicine1);

        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setMedicineId(medicine2);
        patientMedicine2.setQuantity(1);

        allergy.setId(4L);
        allergy.setName("peanut");

        person.setId(28L);
        person.setFirstName("Alexandre");
        person.setLastName("KERESTES");
        calendar.set(1987, 11, 24);
        person.setBirthday(calendar.getTime());
        person.setPhone("1234567890");
        person.setEmail("alexandrekerestes@exemplo.fr");
        person.setMedicines(Arrays.asList(patientMedicine1, patientMedicine2));
        person.setAllergies(Arrays.asList(allergy));

        return person;
    }
}
