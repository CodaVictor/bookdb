package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void BookRepository_Save_ReturnSavedBook() {
        // Arrange
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setIsbn("1-1");

        // Act
        Book savedBook1 = bookRepository.save(book1);

        // Assert
        Assertions.assertNotNull(savedBook1);
        Assertions.assertTrue(savedBook1.getId() > 0);
    }

    @Test
    public void BookRepository_GetAll_ReturnMoreThanOneBook() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setIsbn("1-1");

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setIsbn("2-2");

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        Assertions.assertNotNull(books);
        Assertions.assertEquals(2, books.size());
    }

    @Test
    public void BookRepository_FindById_ReturnBook() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setIsbn("1-1");

        bookRepository.save(book1);

        Book foundBook = bookRepository.findById(book1.getId()).get();

        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals(foundBook.getTitle(), book1.getTitle());
    }

    @Test
    public void BookRepository_ExistsByIsbn_ReturnBook() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setIsbn("1-1");

        bookRepository.save(book1);

        boolean exists = bookRepository.existsByIsbn("1-1");

        Assertions.assertTrue(exists);
    }
}
