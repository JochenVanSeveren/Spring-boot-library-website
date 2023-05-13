package com.hogent.g2a1_vanseveren_jochen;

import config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@SpringBootTest(classes = BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void showBookCatalog() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("isPopularBookCatalog"))
                .andExpect(model().attributeExists("user"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void showMostPopularBooks() throws Exception {
        mockMvc.perform(get("/mostPopularBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("isPopularBookCatalog"))
                .andExpect(model().attributeExists("user"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void showBookDetails() throws Exception {
        mockMvc.perform(get("/bookDetails/978-0-618-00222-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attributeExists("isFavorite"))
                .andExpect(model().attributeExists("userFavoriteLimiteReached"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("user"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Transactional
    void showAddBook() throws Exception {
        mockMvc.perform(get("/addBook/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("globalAuthors"));
    }


}