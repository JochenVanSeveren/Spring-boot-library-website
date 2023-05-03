package com.hogent.g2a1_vanseveren_jochen;

import domain.BookService;
import model.Author;
import model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private List<Book> books;

    @BeforeEach
    public void setUp() {
        Author author1 = new Author(1L,"George Orwell", null);
        Author author2 = new Author(2L,"Harper Lee", null);

        Book book1 = new Book("1984", List.of(author1), "978-0-452-28423-4", 19.99, 4, null);
        Book book2 = new Book("To Kill a Mockingbird", List.of(author2), "978-0-06-093546-7", 24.99, 5, null);

        books = Arrays.asList(book1, book2);
    }

    @Test
    public void showBookCatalog_ShouldReturnIndexViewAndBooks() throws Exception {
        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    public void showBookDetails_ShouldReturnBookDetailsViewAndBook() throws Exception {
        Book book = books.get(0);
        when(bookService.findByIsbn(book.getIsbn())).thenReturn(book);

        mockMvc.perform(get("/bookDetails/{isbn}", book.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attributeExists("user"));
    }


}
