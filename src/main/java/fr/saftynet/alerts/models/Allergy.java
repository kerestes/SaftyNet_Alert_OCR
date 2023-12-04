package fr.saftynet.alerts.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

}
