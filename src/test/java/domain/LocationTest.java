package domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    // Test for valid location
    @Test
    void testValidLocation() {
        Location location = new Location(50, 300, "Test", new Book());

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    // Test for invalid location with plaatscode2 less than 50
    @Test
    void testPlaatscode2LessThan50() {
        Location location = new Location(100, 49, "Test", new Book());

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals("{plaatscode2.range}", violations.iterator().next().getMessage());
    }

    // Test for invalid location with plaatscode2 greater than 300
    @Test
    void testPlaatscode2GreaterThan300() {
        Location location = new Location(50, 350, "Test", new Book());

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals("{plaatscode2.range}", violations.iterator().next().getMessage());
    }

    // Test for invalid location with plaatscode2 less than 50 units from plaatscode1
    @Test
    void testPlaatscode2LessThan50UnitsFromPlaatscode1() {
        Location location = new Location(50, 99, "Test", new Book());

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals("{plaatscode.difference}", violations.iterator().next().getMessage());
    }

    // Test for invalid location with plaatsnaam containing non-letter characters
    @Test
    void testPlaatsnaamContainsNonLetters() {
        Location location = new Location(50, 300, "Test123", new Book());

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals("plaatsnaam should only contain letters", violations.iterator().next().getMessage());
    }
}
