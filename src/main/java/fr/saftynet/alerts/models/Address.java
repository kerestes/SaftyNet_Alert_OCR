package fr.saftynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
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

    @OneToMany(mappedBy = "address", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Person> persons;

    @Transient
    private List<Person> minor = null;

    @Transient
    private List<Person> major = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address1)) return false;
        return Objects.equals(address, address1.address) && Objects.equals(city, address1.city) && Objects.equals(zip, address1.zip);
    }
}
