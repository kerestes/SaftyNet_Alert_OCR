package fr.saftynet.alerts.constans;

public class DBConstants {

    //HQL
    public static final String personsPerFirestation = "SELECT ad FROM Address ad, Firestation f WHERE ad.firestation.id = :firestationId";
    public static final String getPatientMedicineByPersonId = "SELECT pm FROM PatientMedicine pm WHERE pm.personId = :person AND pm.medicineId = :medicine";
    public static final String getEmailPerCity = "SELECT DISTINCT p.email FROM Person p WHERE p.address.city like %:cityName%";
    public static final String getPersonByLastName = "FROM Address ad right join Person p on p.address.id = ad.id where p.lastName like %:lastName%";

    //SQL
    public static final String getCity = "select distinct ad.city from address ad where city like %:city% limit 1";
    public static final String deleteAllergy = "DELETE FROM patient_allergy where person_id = :personId AND allergy_id = :allergyId";
    public static final String deletePatientMedicine = "DELETE FROM patient_medicine as pm WHERE pm.person_id = :personId AND pm.medicine_id = :medicineId";
    public static final String getAddressByName = "SELECT * FROM address ad WHERE ad.address like %:addressName% limit 1";
}
