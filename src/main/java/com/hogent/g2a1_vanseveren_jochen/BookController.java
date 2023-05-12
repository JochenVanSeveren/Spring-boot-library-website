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
import java.util.Optional;
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
        Set<Book> books = bookRepository.findAll();
        User user = userRepository.findByUsername("adminUser");

        model.addAttribute("books", books);
        model.addAttribute("title", "bookcatalog.title");
        model.addAttribute("isPopularBookCatalog", false);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/mostPopularBooks")
    public String showMostPopularBooks(Model model) {
        Set<Book> books = bookRepository.findMostPopularBooks();
        User user = userRepository.findByUsername("adminUser");
        model.addAttribute("books", books);
        model.addAttribute("title", "mostpopular.title");
        model.addAttribute("isPopularBookCatalog", true);
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping("/bookDetails/{isbn}")
    public String showBookDetails(@PathVariable("isbn") String isbn, Model model) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new GenericException("Book not found: ", isbn);
        }
        User user = userRepository.findByUsername("adminUser");
        model.addAttribute("isFavorite", user.getFavoriteBooks().contains(book.get()));
        model.addAttribute("userFavoriteLimiteReached", user.getFavoriteBooks().size() >= user.getFavoriteLimit());
        model.addAttribute("book", book.get());
//        TODO: replace the following line with the code to get the currently logged in user.
        model.addAttribute("user", user);
        return "bookDetails";
    }

    @GetMapping("/addBook/{isbn}")
    public String showAddBook(@PathVariable String isbn, Model model) {
        Optional<Book> book;
        if (isbn.equals("new")) {
            model.addAttribute("isNew", true);
            book = Optional.of(new Book());
        } else {
            book = bookRepository.findByIsbn(isbn);
            if (book.isEmpty()) {
                throw new GenericException("Book not found: ", isbn);
            } else {
                model.addAttribute("isNew", false);
            }
        }
        Set<Author> globalAuthors = authorRepository.findAll();

        model.addAttribute("book", book.get());
        model.addAttribute("globalAuthors", globalAuthors);

        return "bookForm";
    }



    @PostMapping("/addBook")
    public String submitAddBook(
            @Valid
            @ModelAttribute("book") Book book, BindingResult result, Model model,
            @RequestParam("authorNames") String[] authorNames
            , @RequestParam("locationData") String locationData
            , @RequestParam("isNew") boolean isNew
    ) {
        try {
            String isbn = book.getIsbn();

            if (isNew && bookRepository.findByIsbn(isbn).isPresent()) {
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

                Location location = new Location(x, y, name, null);
                if (isNew) {
                    boolean existsAlready = locationRepository.findAll().contains(location);
                    if (existsAlready) {
                        result.rejectValue("locations", "Locations", "Location already exists");
                        log.error("Errors in form, location already exists");
                        log.error(result.toString());
                        Set<Author> globalAuthors = authorRepository.findAll();
                        model.addAttribute("globalAuthors", globalAuthors);
                        return "bookForm";
                    }
                }
                locations.add(location);
            }

            book.setLocations(locations);

            log.info(book.toString());

            Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
            if (existingBook.isPresent()) {
                Book bookToUpdate = existingBook.get();

                // Update the fields of bookToUpdate using the values from book
                bookToUpdate.setTitle(book.getTitle());
                bookToUpdate.setAuthors(book.getAuthors());
                bookToUpdate.setPrice(book.getPrice());
                bookToUpdate.setLocations(book.getLocations());
                // ... and so on for each field you want to update

                book = bookRepository.save(bookToUpdate);
            } else {
                // The book doesn't exist, so save it as a new book
                book = bookRepository.save(book);
            }

// Now that the book has been saved, you can save the locations and authors
            @Valid Book finalBook = book;
            locations.forEach(location -> {
                location.setBook(finalBook);
                locationRepository.save(location);
            });

            @Valid Book finalBook1 = book;
            authors.forEach(author -> {
                Set<Book> books = new HashSet<>(author.getBooks());
                books.add(finalBook1);
                author.setBooks(books);
                authorRepository.save(author);
            });

            return "redirect:/bookDetails/" + isbn;
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

    @PostMapping("/toggleFavorite")
    public String toggleFavorite(@RequestParam("bookIsbn") String isbn, Model model) {
//        TODO: replace with current user
        User user = userRepository.findByUsername("adminUser");
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (user.getFavoriteBooks().contains(book.get())) {
            user.getFavoriteBooks().remove(book.get());
        } else {
            user.getFavoriteBooks().add(book.get());
        }
        userRepository.save(user);
        return "redirect:/bookDetails/" + isbn;
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
