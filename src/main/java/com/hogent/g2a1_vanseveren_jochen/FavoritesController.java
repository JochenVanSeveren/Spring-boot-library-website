//package com.hogent.g2a1_vanseveren_jochen;
//
//import service.BookService;
//import service.FavoritesService;
//import domain.Book;
//import domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import service.UserService;
//
//@Controller
//public class FavoritesController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BookService bookService;
//
//    @Autowired
//    private FavoritesService favoritesService;
//
//    @PostMapping("/toggleFavorite/{bookIsbn}")
//    public String addFavorite(@PathVariable String bookIsbn) {
//        User user = userService.getCurrentUser();
//        Book book = bookService.findByIsbn(bookIsbn);
//        favoritesService.toggleFavorite(user, book);
//        return "redirect:/";
//    }
//}
