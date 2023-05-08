package com.hogent.g2a1_vanseveren_jochen;

import domain.Author;
import domain.Book;
import domain.Location;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import service.AuthorService;
import service.BookService;
import service.LocationService;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Slf4j
public class BookControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private BookService mockBookService;
    @Mock
    private AuthorService mockAuthorService;

    @Mock
    private LocationService mockLocationService;

    @BeforeEach
    void before() {
        MockitoAnnotations.openMocks(this);
        BookController bookControllerUnderTest = new BookController();
        mockMvc = standaloneSetup(bookControllerUnderTest).build();
        ReflectionTestUtils.setField(bookControllerUnderTest, "bookService", mockBookService);
        ReflectionTestUtils.setField(bookControllerUnderTest, "authorService", mockAuthorService);
        ReflectionTestUtils.setField(bookControllerUnderTest, "locationService", mockLocationService);
    }

    @Test
    void submitAddBook() throws Exception {
        // Arrange
        Book expectedBook = new Book("Title", null, "978-0-9936428-4-5", 10, 5, null);
        Author expectedAuthor = new Author(1L, "Author", Set.of(expectedBook));
        expectedBook.setAuthors(Set.of(expectedAuthor));
        expectedBook.setLocations(Set.of(new Location(50, 100, "LOC", expectedBook)));
        Mockito.when(mockBookService.findByIsbn("978-0-9936428-4-5"))
                .then(invocation -> invocation.getMock().toString().contains("-> at ") ? expectedBook : null);
        Mockito.when(mockAuthorService.findByName("Author")).thenReturn(expectedAuthor);
        Mockito.when(mockLocationService.findAll()).thenReturn(new HashSet<>());


        // Define authorNames and locationData
        String[] authorNames = new String[]{"Author"};
        String locationData = "50-100,LOC;";

        // Act & Assert
        mockMvc.perform(post("/addBook")
                        .flashAttr("book", expectedBook)
                        .param("authorNames", authorNames)
                        .param("locationData", locationData))
                .andExpect(status().isFound())
                .andDo(mvcResult -> {
                    if (mvcResult.getResolvedException() != null) {
                        System.out.println("Exception: " + mvcResult.getResolvedException().getMessage());
                        mvcResult.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(redirectedUrl("/bookDetails/" + expectedBook.getIsbn()));



    }


}
