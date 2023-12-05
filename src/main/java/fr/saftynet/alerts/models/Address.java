package fr.saftynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String city;

    private String zip;

    @ManyToOne
    private Firestation firestation;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Person> persons;

    @Transient
    private List<Person> minor = null;

    @Transient
    private List<Person> major = null;
}
