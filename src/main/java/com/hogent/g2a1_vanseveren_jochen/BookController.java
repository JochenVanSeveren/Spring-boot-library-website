package com.hogent.g2a1_vanseveren_jochen;

import domain.Author;
import domain.Book;
import domain.Location;
import domain.User;
import exception.GenericException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.LocationRepository;
import repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@Slf4j
@ComponentScan({"com.hogent.g2a1_vanseveren_jochen", "service", "domain", "exception", "repository", "config", "validation"})
public class BookController {


    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public AuthorRepository authorRepository;


    @Autowired
    public LocationRepository locationRepository;

    @Autowired
    public UserRepository userRepository;

    @GetMapping("/")
    public String showBookCatalog(Model model) {
//        model.addAttribute("books", bookRepository.findAll());
        Set<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", new User("admin", false, "adminUser", "123456789", null));

        return "index";
    }

    @GetMapping("/mostPopularBooks")
    public String showMostPopularBooks(Model model) {
        Set<Book> books = bookRepository.findMostPopularBooks();
        model.addAttribute("books", books);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", new User("admin", false, "adminUser", "123456789", null));

        return "index";
    }

    @GetMapping("/bookDetails/{isbn}")
    public String showBookDetails(@PathVariable("isbn") String isbn, Model model) {
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("book", book);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", new User("admin", false, "adminUser", "123456789", null));

        return "bookDetails";
    }

    @GetMapping("/addBook")
    public String showAddBook(Model model) {

        Book book = new Book();
        Set<Author> globalAuthors = authorRepository.findAll();

        model.addAttribute("book", book);
        model.addAttribute("globalAuthors", globalAuthors);

        return "bookForm";
    }

    @PostMapping("/addBook")
    public String submitAddBook(
            @Valid
            @ModelAttribute("book") Book book, BindingResult result, Model model,
            @RequestParam("authorNames") String[] authorNames
            , @RequestParam("locationData") String locationData
    ) {
        try {
            if (bookRepository.findByIsbn(book.getIsbn()) != null) {
                result.rejectValue("isbn", "Duplicate.book.isbn", "This ISBN already exists");
            }
            if (authorNames.length > Book.MAX_AUTHORS || authorNames.length < 1) {
                result.rejectValue("authors", "Size.book.authors", "There must be between 1 and 3 authors");
            }
            if (locationData == null) {
                result.rejectValue("locations", "Size.book.locations", "There must be at least 1 location");
            }
            if (result.hasErrors()) {
                log.error("Errors in form");
                log.error(result.toString());
                Set<Author> globalAuthors = authorRepository.findAll();
                model.addAttribute("globalAuthors", globalAuthors);
                return "bookForm";
            }


            Set<Author> authors = getAuthorsOrSaveNewAuthors(book, authorNames);
            book.setAuthors(authors);

            Set<Location> locations = new HashSet<>();

            log.debug("locationData" + locationData);

            assert locationData != null;
            String[] items = locationData.split(";");
            for (String item : items) {
                String[] parts = item.split(",");
                String value1 = parts[0].trim();
                String value2 = parts[1].trim();
                int index = value1.indexOf("-");
                int x = Integer.parseInt(value1.substring(0, index));
                int y = Integer.parseInt(value1.substring(index + 1));
                String name = value2.trim();

                log.debug(item, x, y, name);

//                Location location  = locationRepository.findByPlaatscode1Plaatscode2Plaatsnaam(x, y, name);


                Location location = new Location(x, y, name, null);
                boolean existsAlready = locationRepository.findAll().contains(location);
                if (existsAlready) {
                    result.rejectValue("locations", "Locations", "Location already exists");
                    log.error("Errors in form, location already exists");
                    log.error(result.toString());
                    Set<Author> globalAuthors = authorRepository.findAll();
                    model.addAttribute("globalAuthors", globalAuthors);
                    return "bookForm";
                }
//                else locationRepository.save(location);


                locations.add(location);
            }


//            locationRepository.saveAll(locations);

            book.setLocations(locations);

            log.info(book.toString());

            bookRepository.save(book);

            locations.forEach(location -> {
                location.setBook(book);
                locationRepository.save(location);
            });

            authors.forEach(author -> {
                Set<Book> books = new HashSet<>(author.getBooks());
                books.add(book);
                author.setBooks(books);
                authorRepository.save(author);
            });
            return "redirect:/bookDetails/" + book.getIsbn();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenericException("Error in form", e.getMessage());
        }
    }

    private Set<Author> getAuthorsOrSaveNewAuthors(Book book, String[] authorNames) {
        Set<Author> authors = new HashSet<>();
        for (String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName);
            if (author == null) {
                author = new Author();
                author.setName(authorName);
                author.setBooks(new HashSet<>(List.of(book)));
            }
            authors.add(author);
        }
        return authors;
    }

    @ExceptionHandler(GenericException.class)
    public ModelAndView handleCustomException(GenericException ex) {
        ModelAndView model
                = new ModelAndView("error/error");
        model.addObject("errCode", ex.getErrCode());
        model.addObject("errMsg", ex.getErrMsg());
        return model;
    }


}
