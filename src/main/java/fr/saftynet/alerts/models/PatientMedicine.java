package fr.saftynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="patient_medicine")
public class PatientMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private int quantity = 1;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "medicine_id")
    private Medicine medicineId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "person_id")
    private Person personId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientMedicine that)) return false;
        return Objects.equals(medicineId, that.medicineId) && Objects.equals(personId, that.personId);
    }
}
