package fr.saftynet.alerts.integration;

import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.services.MedicineService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class MedicineServiceIT {
    @Autowired
    MedicineService medicineService;

    @Test
    public void getMedicineIT(){
        Optional<Medicine> optionalMedicine = medicineService.getMedicine(1L);
        assertTrue(optionalMedicine.isPresent());
        assertEquals("aznol", optionalMedicine.get().getName());
        assertEquals(60, optionalMedicine.get().getDosage_mg());
    }
}
