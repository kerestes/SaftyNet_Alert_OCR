package fr.saftynet.alerts.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Firestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
