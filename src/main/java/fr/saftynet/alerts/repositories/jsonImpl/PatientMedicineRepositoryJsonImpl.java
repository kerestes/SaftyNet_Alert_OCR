package fr.saftynet.alerts.repositories.jsonImpl;

import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.Address;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.models.Person;
import fr.saftynet.alerts.repositories.IPatientMedicineRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PatientMedicineRepositoryJsonImpl implements IPatientMedicineRepository {
    @Override
    public Optional<PatientMedicine> getPatientMedicineByPersonId(Person person, Medicine medicine) {
        Collection<PatientMedicine> patientMedicineList = JsonReader.getPatientMedicines().values();
        return patientMedicineList.stream()
                .filter(patientMedicine -> patientMedicine.getPersonId().getId().equals(person.getId()) && patientMedicine.getMedicineId().getId().equals(medicine.getId()))
                .findFirst();
    }

    @Override
    public PatientMedicine savePatientMedicine(PatientMedicine patientMedicine) {
        HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
        HashMap<Long, Person> personHashMap = JsonReader.getPersons();
        HashMap<Long, PatientMedicine> patientMedicineHashMap = JsonReader.getPatientMedicines();

        Person person = personHashMap.get(patientMedicine.getPersonId().getId());

        Address address = addressHashMap.get(person.getAddress().getId());
        List<Person> personList = address.getPersons().stream().filter(person1 -> !person1.getId().equals(person.getId())).collect(Collectors.toList());
        address.setPersons(personList);

        List<PatientMedicine> patientMedicineList = person.getMedicines().stream()
                .filter(patientMedicine1 -> patientMedicine1.getId() != null && !patientMedicine1.getId().equals(patientMedicine.getId()))
                .collect(Collectors.toList());

        if(patientMedicine.getId() == null) {
            patientMedicine.setId(patientMedicineHashMap.size() + 1L);
            patientMedicine.setMedicineId(JsonReader.getMedicines().get(patientMedicine.getMedicineId().getId()));
        }

        patientMedicineHashMap.put(patientMedicine.getId(), patientMedicine);

        patientMedicineList.add(patientMedicine);
        person.setMedicines(patientMedicineList);
        personHashMap.put(person.getId(), person);

        address.getPersons().add(person);
        addressHashMap.put(address.getId(), address);

        JsonReader.setAddresses(addressHashMap);
        JsonReader.setPersons(personHashMap);
        JsonReader.setPatientMedicines(patientMedicineHashMap);
        return patientMedicine;
    }

    @Override
    public void deletePatientMedicine(Long personId, Long medicineId) {
        HashMap<Long, PatientMedicine> patientMedicineHashMap = JsonReader.getPatientMedicines();
        Optional<PatientMedicine> optionalPatientMedicine = patientMedicineHashMap.values().stream()
                .filter(patientMedicine -> patientMedicine.getMedicineId().getId().equals(medicineId) && patientMedicine.getPersonId().getId().equals(personId))
                .findFirst();
        if(optionalPatientMedicine.isPresent()){
            HashMap<Long, Address> addressHashMap = JsonReader.getAddresses();
            HashMap<Long, Person> personHashMap = JsonReader.getPersons();

            patientMedicineHashMap.remove(optionalPatientMedicine.get().getId());

            Person person = personHashMap.get(personId);
            List<PatientMedicine> patientMedicineList = person.getMedicines().stream()
                    .filter(patientMedicine -> !patientMedicine.getMedicineId().getId().equals(medicineId)).collect(Collectors.toList());
            person.setMedicines(patientMedicineList);
            personHashMap.put(person.getId(), person);

            Address address = addressHashMap.get(person.getAddress().getId());
            List<Person> personList = address.getPersons().stream()
                    .filter(person1 -> !person1.getId().equals(personId)).collect(Collectors.toList());
            address.setPersons(personList);
            address.getPersons().add(person);
            addressHashMap.put(address.getId(), address);

            JsonReader.setAddresses(addressHashMap);
            JsonReader.setPersons(personHashMap);
            JsonReader.setPatientMedicines(patientMedicineHashMap);
        }
    }
}
