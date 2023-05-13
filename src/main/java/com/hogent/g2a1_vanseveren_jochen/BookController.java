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

import java.security.Principal;
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

    @GetMapping("/books")
    public String showBookCatalog(Model model, Principal principal) {
        Set<Book> books = bookRepository.findAll();
        User user = userRepository.findByUsername(principal.getName());

        model.addAttribute("books", books);
        model.addAttribute("title", "bookcatalog.title");
        model.addAttribute("isPopularBookCatalog", false);
        model.addAttribute("user", user);
        return "books";
    }

    @GetMapping("/mostPopularBooks")
    public String showMostPopularBooks(Model model, Principal principal) {
        Set<Book> books = bookRepository.findMostPopularBooks();
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("books", books);
        model.addAttribute("title", "mostpopular.title");
        model.addAttribute("isPopularBookCatalog", true);
        model.addAttribute("user", user);

        return "books";
    }

    @GetMapping("/bookDetails/{isbn}")
    public String showBookDetails(@PathVariable("isbn") String isbn, Model model, Principal principal) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new GenericException("Book not found: ", isbn);
        }
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("isFavorite", user.getFavoriteBooks().contains(book.get()));
        model.addAttribute("userFavoriteLimiteReached", user.getFavoriteBooks().size() >= user.getFavoriteLimit());
        model.addAttribute("book", book.get());
        model.addAttribute("user", user);
        return "bookDetails";
    }

    @GetMapping("/addBook/{isbn}")
    public String showAddBook(@PathVariable String isbn, Model model, Principal principal) {
        Optional<Book> book;
        User user = userRepository.findByUsername(principal.getName());
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
        model.addAttribute("user", user);

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

            Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
            if (isNew && existingBook.isPresent()) {
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

            if (existingBook.isPresent()) {
                Book bookToUpdate = existingBook.get();

                if (!isNew) {
                    log.debug("Removing locations and authors that are no longer used");

                    Set<Location> oldLocations = new HashSet<>(bookToUpdate.getLocations());
                    oldLocations.removeAll(locations);
                    for (Location location : oldLocations) {
                        log.debug("Removing location: " + location);
                        bookToUpdate.removeLocation(location);
                        locationRepository.delete(location);
                    }

                    Set<Author> oldAuthors = new HashSet<>(bookToUpdate.getAuthors());
                    oldAuthors.removeAll(authors);
                    for (Author author : oldAuthors) {
                        Set<Book> books = new HashSet<>(author.getBooks());
                        books.remove(bookToUpdate);
                        author.setBooks(books);
                        authorRepository.save(author);
                    }
                }

                bookToUpdate.setTitle(book.getTitle());

                // Set new sets of locations and authors
                bookToUpdate.setAuthors(new HashSet<>(authors));
                bookToUpdate.setLocations(new HashSet<>(locations));

                book = bookRepository.save(bookToUpdate);
            } else {
                book = bookRepository.save(book);
            }

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

            log.info("Saved book: " + book);

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
    public String toggleFavorite(@RequestParam("bookIsbn") String isbn, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new GenericException("Book not found: ", isbn);
        }
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
