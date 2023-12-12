package fr.saftynet.alerts.unitaire.models;

import fr.saftynet.alerts.models.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EqualsTest {

    @Test
    public void allergyEqualsTest(){
        Allergy allergy = new Allergy();
        Allergy allergy1 = new Allergy();
        Allergy allergy2 = new Allergy();

        allergy.setId(1L);
        allergy1.setId(2L);
        allergy2.setId(3L);

        allergy.setName("peanut");
        allergy1.setName("peanut");
        allergy2.setName("shellfish");

        assertEquals(allergy, allergy1);
        assertEquals(allergy1, allergy);
        assertNotEquals(allergy, allergy2);
        assertNotEquals(allergy2, allergy1);
    }


}
