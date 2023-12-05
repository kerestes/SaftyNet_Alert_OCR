package fr.saftynet.alerts.models;

import com.fasterxml.jackson.annotation.*;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    private Date birthday;

    @Transient
    private Integer age = null;

    private String phone;

    private String email;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="patient_allergy",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id", referencedColumnName="id"))
    private List<Allergy> allergies = new ArrayList<>();

    @OneToMany(mappedBy = "personId", cascade = {CascadeType.MERGE})
    private List<PatientMedicine> medicines = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @JsonBackReference
    private Address address;

}
