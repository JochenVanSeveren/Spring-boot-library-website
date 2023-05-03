package com.hogent.g2a1_vanseveren_jochen;

import domain.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import model.Book;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class BookController {

    @Autowired
    public BookService bookService;

    @GetMapping("/")
    public String showBookCatalog(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", new User("admin", false));

        boolean isAdmin = false;
        // Uncomment the following lines and implement the 'userService' to check for admin role.
        // if (principal != null) {
        //     Collection<? extends GrantedAuthority> authorities = userService.getAuthorities(principal);
        //     isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        // }
        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }

    @GetMapping("/bookDetails/{isbn}")
    public String showBookDetails(@PathVariable("isbn") String isbn, Model model) {
        Book book = bookService.findByIsbn(isbn);
        model.addAttribute("book", book);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", new User("admin", false));

        return "bookDetails";
    }

    @GetMapping("/addBook")
    public String showAddBook(Model model) {

        Book book = new Book();

        model.addAttribute("book", book);

        return "bookForm";
    }

    @PostMapping("/addBook")
    public String submitAddBook(@Valid @ModelAttribute("book") Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "bookForm";
        }

        bookService.save(book);

        return "redirect:/";
    }


}
