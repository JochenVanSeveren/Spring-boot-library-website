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
        assertEquals("{isbn.error}", violations.iterator().next().getMessage());
    }

    // Test for empty title
    @Test
    void testEmptyTitle() {
        Book book = new Book();
        book.setTitle("");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(10);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());
        assertEquals("{book.title.notBlank}", violations.iterator().next().getMessage());
    }

    // Test for valid price
    @Test
    void testValidPrice() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(50);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertTrue(violations.isEmpty());
    }

    // Test for price less than 1
    @Test
    void testPriceLessThanOne() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(0);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());
        assertEquals("{book.price.min}", violations.iterator().next().getMessage());
    }

    // Test for price greater than 100
    @Test
    void testPriceGreaterThanOneHundred() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(101);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());
        assertEquals("{book.price.max}", violations.iterator().next().getMessage());
    }


}
