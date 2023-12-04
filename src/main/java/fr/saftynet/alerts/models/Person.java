package fr.saftynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @OneToOne
    private Address address;

    private Date birthday;

    @Column
    private String phone;

    @Column
    private String email;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="patient_allergy",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id", referencedColumnName="id"))
    private List<Allergy> allergies = new ArrayList<>();

    @OneToMany(mappedBy = "personId", cascade = {CascadeType.MERGE})
    private List<PatientMedicine> medicines = new ArrayList<>();

}
