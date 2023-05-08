package domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BookTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidIsbn() {
        Book book = new Book();
        book.setIsbn("978-3-16-148410-0");
        book.setTitle("Test Book");
        book.setPrice(10);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidIsbn() {
        Book book = new Book();
        book.setIsbn("978-0-452-28423-5");
        book.setTitle("Test Book");
        book.setPrice(10);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());
        assertEquals("Invalid ISBN number", violations.iterator().next().getMessage());
    }

}
