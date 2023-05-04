package com.hogent.g2a1_vanseveren_jochen;

import service.AuthorService;
import service.BookService;
import service.LocationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import domain.Author;
import domain.Book;
import domain.Location;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;


@Controller
@Slf4j
public class BookController {

    @Autowired
    public BookService bookService;

    @Autowired
    public AuthorService authorService;

    @Autowired
    public LocationService locationService;

    @GetMapping("/")
    public String showBookCatalog(Model model) {
//        model.addAttribute("books", bookRepository.findAll());
        Set<Book> books = bookService.findAll();
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
        Set<Author> globalAuthors = authorService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("globalAuthors", globalAuthors);

        return "bookForm";
    }

    @PostMapping("/addBook")
    public String submitAddBook(
            @Valid
            @ModelAttribute("book") Book book, BindingResult result, Model model,
            @RequestParam("authorNames") String[] authorNames,
            @RequestParam("locationData") String locationData
    ) {


        if (authorNames.length > Book.MAX_AUTHORS || authorNames.length < 1) {
            result.rejectValue("authors", "Size.book.authors", "There must be between 1 and 3 authors");
        }

        if (locationData == null ) {
            result.rejectValue("locations", "Size.book.locations", "There must be at least 1 location");
        }

        if (result.hasErrors()) {

            log.error("Errors in form");
            log.error(result.toString());
            Set<Author> globalAuthors = authorService.findAll();
            model.addAttribute("globalAuthors", globalAuthors);
            return "bookForm";
        }


        Set<Author> authors = new HashSet<>();
        for (String authorName : authorNames) {
            Author author = authorService.findByName(authorName);
            if (author == null) {
                author = new Author();
                author.setName(authorName);
                authorService.save(author);
            }
            authors.add(author);
        }


        book.setAuthors(authors);


        log.info(locationData);

        Set<Location> locations = new HashSet<>();

        assert locationData != null;
        String[] items = locationData.split(";");
        for (String item : items) {
            String[] parts = item.split(",");
            String value1 = parts[0].trim();
            String value2 = parts[1].trim();
            int index = value1.indexOf("-");
            int x = Integer.parseInt(value1.substring(0, index));
            int y = Integer.parseInt(value1.substring(index + 1));
            String name = value2.substring(0, value2.length() - 1).trim();
            Location location = new Location(x, y, name, book);
            locations.add(location);
        }

        book.setLocations(locations);

        log.info(book.toString());

        bookService.save(book);

        return "redirect:/";
    }




}
