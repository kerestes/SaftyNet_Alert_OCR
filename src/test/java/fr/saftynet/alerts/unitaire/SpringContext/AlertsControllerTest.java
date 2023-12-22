package fr.saftynet.alerts.unitaire.SpringContext;

import fr.saftynet.alerts.models.*;
import fr.saftynet.alerts.services.AddressService;
import fr.saftynet.alerts.services.FirestationService;
import fr.saftynet.alerts.services.PersonService;
import fr.saftynet.alerts.utilities.AddressUtility;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AlertsControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    FirestationService firestationService;

    @MockBean
    AddressService addressService;

    @MockBean
    PersonService personService;

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
                        jsonPath("$").doesNotExist()

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
                        jsonPath("$").doesNotExist()
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
                        jsonPath("$").doesNotExist()
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
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void getPersonByAddressWithMedicalRecordsTest() throws Exception {
        when(addressService.getAddressByName(any())).thenReturn(makeAddress());
        mockMvc.perform(get("/fire?address={address}", "112 Steppes"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.address", is("112 Steppes Pl")),
                        jsonPath("$.firestation.name", is("Firestation 4")),
                        jsonPath("$.persons", hasSize(3)),
                        jsonPath("$.persons.[0].lastName", containsStringIgnoringCase("Cooper")),
                        jsonPath("$.persons.[2].allergies.[0].name", is("nillacilan"))
                );
    }

    @Test
    public void getPersonByAddressWithMedicalRecordsNoAddressTest() throws Exception {
        mockMvc.perform(get("/fire"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void getPhoneAlertTest() throws Exception {
        Optional<Firestation> optionalFirestation = Optional.of(new Firestation());
        optionalFirestation.get().setId(1L);
        optionalFirestation.get().setName("Firestation 1");

        when(firestationService.getFirestation(anyLong())).thenReturn(optionalFirestation);
        when(firestationService.getPersonsPerFirestation(1L)).thenReturn(makeAddressList());

        mockMvc.perform(get("/phoneAlert?firestationId={id}", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("['Firestation 1']", CoreMatchers.hasItem("8418747462")),
                        jsonPath("['Firestation 1']", hasSize(5))
                );

    }

    @Test
    public void getPhoneAlertNotBodyRequestTest() throws Exception {

        mockMvc.perform(get("/phoneAlert"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );

    }

    @Test
    public void getPhoneAlertInvalidIdTest() throws Exception {

        when(firestationService.getFirestation(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/phoneAlert?firestationId={id}", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );

    }

    @Test
    public void getFirestationTest() throws Exception {
        when(firestationService.getPersonsPerFirestation(1L)).thenReturn(makeAddressList());

        mockMvc.perform(get("/firestation?stationNumber={id}", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].address", CoreMatchers.is("644 Gershwin Cir")),
                        jsonPath("$[0].major.[0].lastName", CoreMatchers.is("DUCAN")),
                        jsonPath("$[0].minor", hasSize(0)),
                        jsonPath("$[1].id", CoreMatchers.is(9))
                );
    }

    @Test
    public void getFirestationNoStationNumberTest() throws Exception {

        mockMvc.perform(get("/firestation"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    @Test
    public void getFirestationNoFirestationTest() throws Exception {
        when(firestationService.getPersonsPerFirestation(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/firestation?stationNumber={id}", 100))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist()
                );
    }

    public List<Address> makeAddressList(){
        List<Address> addresses = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        Address address = new Address();
        address.setId(4L);
        address.setAddress("644 Gershwin Cir");
        address.setCity("Culver");
        address.setZip("97451");

        Address address1 = new Address();
        address1.setId(9L);
        address1.setAddress("908 73rd St");
        address1.setCity("Culver");
        address1.setZip("97451");

        Address address2 = new Address();
        address2.setId(10L);
        address2.setAddress("947 E. Rose Dr");
        address2.setCity("Culver");
        address2.setZip("97451");

        Address address3 = new Address();
        address2.setId(11L);
        address2.setAddress("951 LoneTree Rd");
        address2.setCity("Culver");
        address2.setZip("97451");

        Person person1 = new Person();
        person1.setFirstName("Brain");
        person1.setLastName("STELZER");
        calendar.set(1975, 12, 06);
        person1.setBirthday(calendar.getTime());
        person1.setPhone("8418747784");

        Person person2 = new Person();
        person2.setFirstName("Shawna");
        person2.setLastName("STELZER");
        calendar.set(1980, 07, 8);
        person2.setBirthday(calendar.getTime());
        person2.setPhone("8418747784");

        Person person3 = new Person();
        person3.setFirstName("Kendrik");
        person3.setLastName("STELZER");
        calendar.set(2014, 3, 6);
        person3.setBirthday(calendar.getTime());
        person3.setPhone("8418747784");

        Person person4 = new Person();
        person4.setFirstName("Jamie");
        person4.setLastName("PETERS");
        calendar.set(1982, 3, 6);
        person4.setBirthday(calendar.getTime());
        person4.setPhone("8418747462");

        Person person5 = new Person();
        person5.setFirstName("Eric");
        person5.setLastName("CADIGAN");
        calendar.set(1975, 8, 6);
        person5.setBirthday(calendar.getTime());
        person5.setPhone("8418747458");

        Person person6 = new Person();
        person6.setFirstName("Peter");
        person6.setLastName("DUCAN");
        calendar.set(2000, 9, 6);
        person6.setBirthday(calendar.getTime());
        person6.setPhone("8418746512");

        Person person7 = new Person();
        person7.setFirstName("Reginold");
        person7.setLastName("WALKER");
        calendar.set(1979, 8, 30);
        person7.setBirthday(calendar.getTime());
        person7.setPhone("8418748547");

        address3.setPersons(Arrays.asList(person5));
        address2.setPersons(Arrays.asList(person1, person2, person3));
        address1.setPersons(Arrays.asList(person4, person7));
        address.setPersons(Arrays.asList(person6));

        addresses.add(address);
        addresses.add(address1);
        addresses.add(address2);
        addresses.add(address3);

        return addresses;
    }

    private Optional<Address> makeAddress() {
        Optional<Address> optionalAddress = Optional.of(new Address());
        Calendar calendar = Calendar.getInstance();

        Firestation firestation = new Firestation();
        firestation.setName("Firestation 4");
        firestation.setId(4L);

        Medicine medicine1 = new Medicine();
        medicine1.setId(5L);
        medicine1.setName("hydrapermazol");
        medicine1.setDosage_mg(100);

        Medicine medicine2 = new Medicine();
        medicine2.setId(4L);
        medicine2.setName("dodoxadin");
        medicine2.setDosage_mg(30);

        PatientMedicine patientMedicine1 = new PatientMedicine();
        patientMedicine1.setQuantity(3);
        patientMedicine1.setMedicineId(medicine1);

        PatientMedicine patientMedicine2 = new PatientMedicine();
        patientMedicine2.setQuantity(1);
        patientMedicine2.setMedicineId(medicine2);

        Allergy allergy1 = new Allergy();
        allergy1.setId(5L);
        allergy1.setName("shellfish");

        Person person1 = new Person();
        person1.setId(10L);
        person1.setFirstName("Tony");
        person1.setLastName("COOPER");
        calendar.set(1994, 3, 6);
        person1.setBirthday(calendar.getTime());
        person1.setPhone("8418746874");
        person1.setEmail("tcoop@ymail.com");
        person1.setMedicines(Arrays.asList(patientMedicine1, patientMedicine2));
        person1.setAllergies(Arrays.asList(allergy1));

        Person person2 = new Person();
        person2.setId(17L);
        person2.setFirstName("Ron");
        person2.setLastName("PETERS");
        calendar.set(1965, 4, 6);
        person2.setBirthday(calendar.getTime());
        person2.setPhone("8418748888");
        person2.setEmail("jpeter@email.com");

        Allergy allergy2 = new Allergy();
        allergy2.setId(3L);
        allergy2.setName("nillacilan");

        Medicine medicine3 = new Medicine();
        medicine3.setId(2L);
        medicine3.setName("aznol");
        medicine3.setDosage_mg(200);

        PatientMedicine patientMedicine3 = new PatientMedicine();
        patientMedicine3.setQuantity(1);
        patientMedicine3.setMedicineId(medicine3);

        Person person3 = new Person();
        person3.setId(18L);
        person3.setFirstName("Allison");
        person3.setLastName("BOYD");
        calendar.set(1965, 3, 15);
        person3.setBirthday(calendar.getTime());
        person3.setPhone("8418749888");
        person3.setEmail("aly@imail.com");
        person3.setAllergies(Arrays.asList(allergy2));
        person3.setMedicines(Arrays.asList(patientMedicine3));

        optionalAddress.get().setId(6L);
        optionalAddress.get().setAddress("112 Steppes Pl");
        optionalAddress.get().setCity("Culver");
        optionalAddress.get().setZip("97451");
        optionalAddress.get().setFirestation(firestation);
        optionalAddress.get().setPersons(Arrays.asList(person1, person2, person3));
        return optionalAddress;
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
}
