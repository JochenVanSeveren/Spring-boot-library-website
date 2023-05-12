package repository;

import domain.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Set<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b, COUNT(u) as cnt FROM Book b LEFT JOIN b.users u GROUP BY b.id, b.title HAVING COUNT(u) > 0 ORDER BY cnt DESC, b.title ASC")
    Set<Book> findMostPopularBooks();

}
