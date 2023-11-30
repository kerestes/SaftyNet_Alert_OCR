package fr.saftynet.alerts.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;

    private String city;

    private String zip;

    @ManyToOne
    private Firestation firestation;
}
