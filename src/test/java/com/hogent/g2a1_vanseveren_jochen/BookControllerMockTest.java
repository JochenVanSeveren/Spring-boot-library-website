package com.hogent.g2a1_vanseveren_jochen;

import config.SecurityConfig;
import domain.Author;
import domain.Book;
import domain.Location;
import domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.LocationRepository;
import repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Import(SecurityConfig.class)
@Slf4j
public class BookControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private BookRepository mockBookRepository;
    @Mock
    private AuthorRepository mockAuthorRepository;

    @Mock
    private LocationRepository mockLocationRepository;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void before() {
        MockitoAnnotations.openMocks(this);
        BookController bookControllerUnderTest = new BookController();
        mockMvc = standaloneSetup(bookControllerUnderTest).build();
        ReflectionTestUtils.setField(bookControllerUnderTest, "bookRepository", mockBookRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "authorRepository", mockAuthorRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "locationRepository", mockLocationRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "userRepository", mockUserRepository);
//        ReflectionTestUtils.setField(bookControllerUnderTest, "globalAuthors", new HashSet<>());
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    void submitAddBook() throws Exception {
        // Arrange
        Book expectedBook = new Book("Title", null, "978-0-9936428-4-5", 10, 5, null);
        Author expectedAuthor = new Author(1L, "Author", Set.of(expectedBook));
        expectedBook.setAuthors(Set.of(expectedAuthor));
        expectedBook.setLocations(Set.of(new Location(50, 100, "LOC", expectedBook)));
        Mockito.when(mockBookRepository.findByIsbn("978-0-9936428-4-5"))
                .then(invocation -> invocation.getMock().toString().contains("-> at ")
                        ? Optional.of(expectedBook)
                        : Optional.empty());
        Mockito.when(mockAuthorRepository.findByName("Author")).thenReturn(expectedAuthor);
        Mockito.when(mockLocationRepository.findAll()).thenReturn(new HashSet<>());
//        Mockito.when(mockUserRepository.findByUsername("user")).thenReturn(new User("admin", false, "adminUser", "123456789", null));

        // Define authorNames and locationData
        String[] authorNames = new String[]{"Author"};
        String locationData = "50-100,LOC;";

        // Act & Assert
        mockMvc.perform(post("/addBook")
                        .flashAttr("book", expectedBook)
                        .param("authorNames", authorNames)
                        .param("locationData", locationData)
                        .param("isNew", String.valueOf(true)))
                .andExpect(status().isFound())
                .andDo(mvcResult -> {
                    if (mvcResult.getResolvedException() != null) {
                        System.out.println("Exception: " + mvcResult.getResolvedException().getMessage());
                        mvcResult.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(redirectedUrl("/bookDetails/" + expectedBook.getIsbn()));


    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void toggleFavorite() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("user");
        user.setRole("USER");
        user.setFavoriteBooks(new HashSet<>());
        Book book = new Book();
        book.setIsbn("978-0-9936428-4-5");
        user.getFavoriteBooks().add(book);
        Mockito.when(mockUserRepository.findByUsername("user")).thenReturn(user);
        Mockito.when(mockBookRepository.findByIsbn("978-0-9936428-4-5")).thenReturn(Optional.of(book));

        // Act & Assert
        mockMvc.perform(post("/toggleFavorite")
                        .param("bookIsbn", "978-0-9936428-4-5"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bookDetails/978-0-9936428-4-5"))
                .andDo(mvcResult -> {
                    // Check if the book was removed from favorites
                    assertTrue(user.getFavoriteBooks().isEmpty());
                    // Check if the book was added to favorites
                    mockMvc.perform(post("/toggleFavorite")
                                    .param("bookIsbn", "978-0-9936428-4-5"))
                            .andExpect(status().isFound())
                            .andExpect(redirectedUrl("/bookDetails/978-0-9936428-4-5"))
                            .andDo(innerMvcResult -> {
                                // Check if the book was added to favorites
                                assertTrue(user.getFavoriteBooks().contains(book));
                            });
                });
    }


}
