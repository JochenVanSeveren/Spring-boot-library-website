package service;

import domain.Book;
import domain.User;

public interface FavoritesService {
    void toggleFavorite(User user, Book book);
}
