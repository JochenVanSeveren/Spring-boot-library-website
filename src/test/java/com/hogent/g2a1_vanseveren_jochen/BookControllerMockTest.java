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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.LocationRepository;
import repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
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

    @Mock
    private MessageSource mockMessageSource;

    @BeforeEach
    void before() {
        MockitoAnnotations.openMocks(this);
        BookController bookControllerUnderTest = new BookController();
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookControllerUnderTest)
//                .apply(springSecurity()) // apply Spring Security to MockMvc
                .build();
        ReflectionTestUtils.setField(bookControllerUnderTest, "bookRepository", mockBookRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "authorRepository", mockAuthorRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "locationRepository", mockLocationRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "userRepository", mockUserRepository);
        ReflectionTestUtils.setField(bookControllerUnderTest, "messageSource", mockMessageSource);

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void submitAddBook() throws Exception {
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new TestingAuthenticationToken("admin", "admin", "ROLE_ADMIN");
            }

            @Override
            public void setAuthentication(Authentication authentication) {
            }
        });
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void toggleFavorite() throws Exception {
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new TestingAuthenticationToken("admin", "admin", "ROLE_ADMIN");
            }

            @Override
            public void setAuthentication(Authentication authentication) {
            }
        });

        User user = new User();
        user.setUsername("admin");
        user.setFavoriteBooks(new HashSet<>());
        Book book = new Book();
        book.setIsbn("978-0-9936428-4-5");
        user.getFavoriteBooks().add(book);

        Mockito.when(mockUserRepository.findByUsername("admin")).thenReturn(user);
        Mockito.when(mockBookRepository.findByIsbn("978-0-9936428-4-5")).thenReturn(Optional.of(book));

        // Act & Assert
        mockMvc.perform(post("/toggleFavorite")
                        .with(csrf().asHeader())
                        .param("bookIsbn", "978-0-9936428-4-5")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andDo(mvcResult -> {
                    // Check if the book was removed from favorites
                    assertTrue(user.getFavoriteBooks().isEmpty());
                    // Check if the book was added to favorites
                    mockMvc.perform(post("/toggleFavorite")
                                    .param("bookIsbn", "978-0-9936428-4-5"))
                            .andExpect(status().isFound())
                            .andExpect(redirectedUrl("/books"))
                            .andDo(innerMvcResult -> {
                                // Check if the book was added to favorites
                                assertTrue(user.getFavoriteBooks().contains(book));
                            });
                });
    }


}
