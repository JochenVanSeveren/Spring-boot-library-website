package domain;

import model.Book;
import model.User;

public interface FavoritesService {
    void toggleFavorite(User user, Book book);
}
