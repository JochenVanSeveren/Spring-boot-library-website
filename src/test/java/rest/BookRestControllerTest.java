package rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogent.g2a1_vanseveren_jochen.G2A1VanSeverenJochenApplication;
import domain.Author;
import domain.Book;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import repository.AuthorRepository;
import repository.BookRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@SpringBootTest(classes = G2A1VanSeverenJochenApplication.class)
@AutoConfigureMockMvc
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    public void getBooksByAuthorTest() throws Exception {
        Author author = new Author("Test Author", new HashSet<>());
        Book book1 = new Book("Test Book 1", new HashSet<>(), "1234567890123", 10.0, 5, new HashSet<>());
        Book book2 = new Book("Test Book 2", new HashSet<>(), "1234567890124", 15.0, 5, new HashSet<>());
        author.getBooks().addAll(Arrays.asList(book1, book2));

        Mockito.when(authorRepository.findByName(Mockito.anyString())).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/byAuthor/Test Author")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void getBookByIsbnTest() throws Exception {
        Book book = new Book("Test Book 1", new HashSet<>(), "1234567890123", 10.0, 5, new HashSet<>());

        Mockito.when(bookRepository.findByIsbn(Mockito.anyString())).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/byIsbn/1234567890123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Book 1"));
    }

    @Test
    public void getBookByIsbnNotFoundTest() throws Exception {
        Mockito.when(bookRepository.findByIsbn(Mockito.anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/byIsbn/1234567890123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getBooksByAuthorNotFoundTest() throws Exception {
        Mockito.when(authorRepository.findByName(Mockito.anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/byAuthor/Test Author")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
