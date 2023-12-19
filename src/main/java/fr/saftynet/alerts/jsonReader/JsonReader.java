package fr.saftynet.alerts.jsonReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.saftynet.alerts.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.SerializationUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonReader {
    private static HashMap<Long, Address> addresses;
    private static HashMap<Long, Person> persons;
    private static HashMap<Long, Allergy> allergies;
    private static HashMap<Long, Firestation> firestations;
    private static HashMap<Long, Medicine> medicines;
    private static HashMap<Long, PatientMedicine> patientMedicines;
    private static JsonNode jsonNode;

    private static final Logger logger = LogManager.getLogger(JsonReader.class);

    private static void initJson() {

        addresses = new HashMap<>();
        persons = new HashMap<>();
        allergies = new HashMap<>();
        firestations = new HashMap<>();
        medicines = new HashMap<>();
        patientMedicines = new HashMap<>();

        logger.debug("Json file in memory initialization");

        String json = "";
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("src/main/resources/data.json");
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);
            String linha = br.readLine();
            while(linha != null) {
                json += linha;
                linha = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parseJson(json);
    }

    public static HashMap<Long, Address> getAddresses() {
        if(jsonNode == null)
            initJson();
        return SerializationUtils.clone(addresses);
    }

    public static HashMap<Long, Person> getPersons() {
        if(jsonNode == null)
            initJson();
        return SerializationUtils.clone(persons);
    }

    public static HashMap<Long, Allergy> getAllergies() {
        if(jsonNode == null)
            initJson();
        return SerializationUtils.clone(allergies);
    }

    public static HashMap<Long, Firestation> getFirestations() {
        if(jsonNode == null)
            initJson();
        return SerializationUtils.clone(firestations);
    }

    public static HashMap<Long, Medicine> getMedicines() {
        if(jsonNode == null)
            initJson();
        return SerializationUtils.clone(medicines);
    }

    public static HashMap<Long, PatientMedicine> getPatientMedicines() {
        if(jsonNode == null)
            initJson();
        return SerializationUtils.clone(patientMedicines);
    }

    public static void setAddresses(HashMap<Long, Address> addresses) {
        JsonReader.addresses = new HashMap<>(addresses);
    }

    public static void setPersons(HashMap<Long, Person> persons) {
        JsonReader.persons = new HashMap<>(persons);
    }

    public static void setAllergies(HashMap<Long, Allergy> allergies) {
        JsonReader.allergies = new HashMap<>(allergies);
    }

    public static void setFirestations(HashMap<Long, Firestation> firestations) {
        JsonReader.firestations = new HashMap<>(firestations);
    }

    public static void setMedicines(HashMap<Long, Medicine> medicines) {
        JsonReader.medicines = new HashMap<>(medicines);
    }

    public static void setPatientMedicines(HashMap<Long, PatientMedicine> patientMedicines) {
        JsonReader.patientMedicines = new HashMap<>(patientMedicines);
    }

    private static void parseJson(String json){

        ObjectMapper mapper = new ObjectMapper();

        try {
            jsonNode = new ObjectMapper().readTree(json);
            jsonNode.get("Firestation").forEach(firestation -> {
                try {
                    firestations.put(firestation.get("id").asLong(), mapper.treeToValue(firestation, Firestation.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonNode.get("Address").forEach(addresse -> {
                Address realAddress = new Address();
                realAddress.setAddress(addresse.get("address").asText());
                realAddress.setCity(addresse.get("city").asText());
                realAddress.setId(addresse.get("id").asLong());
                realAddress.setZip(addresse.get("zip").asText());
                realAddress.setPersons(new ArrayList<>());
                realAddress.setFirestation(firestations.get(addresse.get("firestation").asLong()));
                addresses.put(realAddress.getId(), realAddress);
            });
            jsonNode.get("Medicine").forEach(medicine -> {
                try {
                    medicines.put(medicine.get("id").asLong(), mapper.treeToValue(medicine, Medicine.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonNode.get("Allergy").forEach(allergy -> {
                try {
                    allergies.put(allergy.get("id").asLong(), mapper.treeToValue(allergy, Allergy.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonNode.get("Person").forEach(person -> {
                Person newPerson = new Person();
                newPerson.setAddress(addresses.get(person.get("address").asLong()));
                newPerson.setId(person.get("id").asLong());
                newPerson.setEmail(person.get("email").asText());
                newPerson.setFirstName(person.get("first_name").asText());
                newPerson.setLastName(person.get("last_name").asText());
                newPerson.setPhone(person.get("phone").asText());

                try {
                    newPerson.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(person.get("birthday").asText()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                newPerson.setAllergies(new ArrayList<>());
                if(person.has("allergy")){
                    person.get("allergy").forEach(allergyId -> {
                        newPerson.getAllergies().add(allergies.get(allergyId.asLong()));
                    });
                }

                newPerson.setMedicines(new ArrayList<>());
                if(person.has("medicine")){
                    person.get("medicine").forEach(medicine -> {
                        PatientMedicine patientMedicine = new PatientMedicine();
                        patientMedicine.setId(patientMedicines.size() + 1L);
                        patientMedicine.setMedicineId(medicines.get(medicine.get("id").asLong()));
                        patientMedicine.setPersonId(newPerson);
                        patientMedicine.setQuantity(medicine.get("quantity").asInt());
                        patientMedicines.put(patientMedicine.getId(), patientMedicine);
                        newPerson.getMedicines().add(patientMedicine);
                    });
                }
                addresses.get(person.get("address").asLong()).getPersons().add(newPerson);
                persons.put(newPerson.getId(), newPerson);
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
