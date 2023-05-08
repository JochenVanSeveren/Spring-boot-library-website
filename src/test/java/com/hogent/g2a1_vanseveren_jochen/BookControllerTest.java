package com.hogent.g2a1_vanseveren_jochen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showBookCatalog() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void showBookDetails() throws Exception {
        mockMvc.perform(get("/bookDetails/978-0-618-00222-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void showAddBook() throws Exception {
        mockMvc.perform(get("/addBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("globalAuthors"));
    }

}