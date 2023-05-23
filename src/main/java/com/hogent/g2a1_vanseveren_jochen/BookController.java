package com.hogent.g2a1_vanseveren_jochen;

import domain.Author;
import domain.Book;
import domain.Location;
import domain.User;
import exception.GenericException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.LocationRepository;
import repository.UserRepository;

import java.util.*;


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
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/books")
    public String showBookCatalog(Model model) {
        Set<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("title", "bookcatalog.title");
        model.addAttribute("isPopularBookCatalog", false);
        return "books";
    }

    @GetMapping("/mostPopularBooks")
    public String showMostPopularBooks(Model model) {
        Set<Book> books = bookRepository.findMostPopularBooks();
        model.addAttribute("books", books);
        model.addAttribute("title", "mostpopular.title");
        model.addAttribute("isPopularBookCatalog", true);
        return "books";
    }

    @GetMapping("/bookDetails/{isbn}")
    public String showBookDetails(@PathVariable("isbn") String isbn, Model model, Authentication authentication) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new GenericException("Book not found: ", isbn);
        }
        User user = userRepository.findByUsername(authentication.getName());
        model.addAttribute("isFavorite", user.getFavoriteBooks().contains(book.get()));
        model.addAttribute("userFavoriteLimiteReached", user.getFavoriteBooks().size() >= user.getFavoriteLimit());
        model.addAttribute("book", book.get());
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
            if (errorRedirect(result, model, isNew)) return "bookForm";


            Set<Author> authors = getAuthorsOrSaveNewAuthors(authorNames);
            book.setAuthors(authors);

            Set<Location> locations = new HashSet<>();
            assert locationData != null;
            formatLocations(result, model, locationData, isNew, locations);

            if (locations.size() > Book.MAX_LOCATIONS) {
                result.rejectValue("locations", "Size.book.locations", "There must be at most 3 locations");
            }

            if (errorRedirect(result, model, isNew)) return "bookForm";

            book.setLocations(locations);

            book = saveBook(book, isNew, existingBook, authors, locations);

            saveAuthorsAndLocations(book, authors, locations);

            return "redirect:/bookDetails/" + isbn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenericException("Error in form", e.getMessage());
        }
    }

    private void formatLocations(BindingResult result, Model model, String locationData, boolean isNew, Set<Location> locations) {
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
                boolean existsAlready = (locationRepository.findAll().contains(location));
                if (existsAlready) {
                    result.rejectValue("locations", "Locations", "Location already exists");
                    log.error("Errors in form, location already exists");
                    log.error(result.toString());
                    Set<Author> globalAuthors = authorRepository.findAll();
                    model.addAttribute("globalAuthors", globalAuthors);
                    model.addAttribute("isNew", true);
                }
            }
            locations.add(location);
        }
    }

    private Book saveBook(Book book, boolean isNew, Optional<Book> existingBook, Set<Author> authors, Set<Location> locations) {
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
            bookToUpdate.setPrice(book.getPrice());
            // Set new sets of locations and authors
            bookToUpdate.setAuthors(new HashSet<>(authors));
            bookToUpdate.setLocations(new HashSet<>(locations));

            book = bookRepository.save(bookToUpdate);
        } else {
            book = bookRepository.save(book);
        }
        return book;
    }

    private Set<Author> getAuthorsOrSaveNewAuthors(String[] authorNames) {
        Set<Author> authors = new HashSet<>();
        for (String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName);
            if (author == null) {
                author = new Author();
                author.setName(authorName);
            }
            authors.add(author);
        }
        return authors;
    }

    private void saveAuthorsAndLocations(Book book, Set<Author> authors, Set<Location> locations) {
        @Valid Book finalBook = book;
        locations.forEach(location -> {
            location.setBook(finalBook);
            locationRepository.save(location);
        });

        authors.forEach(author -> {
            Set<Book> books = new HashSet<>(author.getBooks());
            books.add(finalBook);
            author.setBooks(books);
            authorRepository.save(author);
        });
    }

    private boolean errorRedirect(BindingResult result, Model model, @RequestParam("isNew") boolean isNew) {
        if (result.hasErrors()) {
            log.error("Errors in form");
            log.error(result.toString());
            Set<Author> globalAuthors = authorRepository.findAll();
            model.addAttribute("globalAuthors", globalAuthors);
            model.addAttribute("isNew", isNew);
            return true;
        }
        return false;
    }


    @PostMapping("/toggleFavorite")
    public String toggleFavorite(@RequestParam("bookIsbn") String isbn, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new GenericException("Book not found: ", isbn);
        }
        Locale locale = RequestContextUtils.getLocale(request);
        String message;
        if (user.getFavoriteBooks().contains(book.get())) {
            user.getFavoriteBooks().remove(book.get());
            message = messageSource.getMessage("book.removed", new Object[]{book.get().getTitle()}, locale);
        } else {
            user.getFavoriteBooks().add(book.get());
            message = messageSource.getMessage("book.added", new Object[]{book.get().getTitle()}, locale);
        }
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/books";
    }


    @ExceptionHandler(GenericException.class)
    public ModelAndView handleCustomException(GenericException ex) {
        ModelAndView model
                = new ModelAndView("error/error");
        model.addObject("errCode", ex.getErrCode());
        model.addObject("errMsg", ex.getErrMsg());
        return model;
    }

    @ModelAttribute("userListRoles")
    public List<String> getUserListRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
    }

    @ModelAttribute("username")
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
