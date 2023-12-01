package fr.saftynet.alerts.controllers;

import fr.saftynet.alerts.models.Medicalrecord;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.models.PatientMedicine;
import fr.saftynet.alerts.services.MedicalrecordService;
import fr.saftynet.alerts.services.PatientMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class MedicalrecordController {

    @Autowired
    private MedicalrecordService medicalrecordService;

    @Autowired
    private PatientMedicineService patientMedicineService;

    @PostMapping("/medicalrecord")
    public Medicalrecord addMedicalrecord(@RequestBody Medicalrecord medicalrecord){
        Medicalrecord newMedicalrecord = medicalrecordService.addMedicalrecord(medicalrecord);
        List<PatientMedicine> patientMedicines = new ArrayList<>();
        medicalrecord.getMedicines().forEach(patientMedicine -> {
            Medicine medicine = new Medicine();
            medicine.setId(patientMedicine.getMedicineId().getId());
            patientMedicine.setMedicalrecordId(newMedicalrecord);
            patientMedicine.setMedicineId(medicine);
            patientMedicineService.savePatientMedicine(patientMedicine);
            patientMedicines.add(patientMedicine);
        });
        newMedicalrecord.setMedicines(patientMedicines);
        return newMedicalrecord;
    }

    @PutMapping("/medicalrecord")
    public Medicalrecord updateMedicalrecord(@RequestBody Medicalrecord medicalrecord){
        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordService.getMedicalrecord(medicalrecord.getId());
        if(optionalMedicalrecord.isPresent()){
            return medicalrecordService.updateMedicalrecord(medicalrecord, optionalMedicalrecord.get());
        }
        return null;
    }

    @PutMapping("/medicalrecord/{id}")
    public Medicalrecord updateMedicalrecordId(@RequestBody Medicalrecord medicalrecord, @PathVariable Long id){
        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordService.getMedicalrecord(id);
        if(optionalMedicalrecord.isPresent()){
            medicalrecord.setId(id);
            return medicalrecordService.updateMedicalrecord(medicalrecord, optionalMedicalrecord.get());
        }
        return null;
    }

    @DeleteMapping("/medicalrecord/{id}")
    public void deleteMedicalrecord(@PathVariable final Long id){medicalrecordService.deleteMedicalrecord(id);}
}
