package fr.saftynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Medicalrecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="first_name") @NotNull @NotEmpty
    private String firstName;

    @Column(name="last_name") @NotNull @NotEmpty
    private String lastName;

    @Column @NotNull
    private Date birthday;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name="patient_allergie",
            joinColumns = @JoinColumn(name = "medicalrecord_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "allergie_id", referencedColumnName="id"))
    private List<Allergie> allergies;

    @OneToMany(mappedBy = "medicalrecordId", cascade = {CascadeType.MERGE})
    private List<PatientMedicine> medicines;
}
