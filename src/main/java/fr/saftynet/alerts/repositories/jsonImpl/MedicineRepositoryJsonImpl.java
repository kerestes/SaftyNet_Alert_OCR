package fr.saftynet.alerts.repositories.jsonImpl;

import fr.saftynet.alerts.jsonReader.JsonReader;
import fr.saftynet.alerts.models.Medicine;
import fr.saftynet.alerts.repositories.IMedicineRepository;

import java.util.HashMap;
import java.util.Optional;

public class MedicineRepositoryJsonImpl implements IMedicineRepository {
    @Override
    public Optional<Medicine> getMedicine(Long id) {
        return Optional.ofNullable(JsonReader.getMedicines().get(id));
    }

    @Override
    public Medicine saveMedicine(Medicine medicine) {
        if(medicine.getId() == null && medicine.getName() != null && medicine.getDosage_mg() > 0){
            HashMap<Long, Medicine> medicineHashMap = JsonReader.getMedicines();
            medicine.setId(medicineHashMap.size() + 1L);
            medicineHashMap.put(medicine.getId(), medicine);
            JsonReader.setMedicines(medicineHashMap);
        }
        return JsonReader.getMedicines().get(medicine.getId());
    }
}
